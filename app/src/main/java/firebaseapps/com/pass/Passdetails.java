package firebaseapps.com.pass;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class Passdetails extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView scan_id;
    private ProgressDialog mDialog;
    private Uri scaniduri;
    private Uri profilephoto;
    private final int PROFILE_PHOTO=1321;       //To recognise in onActivityresult
    private final int SCAN_ID=12241;            //To recognise in onActivityResult
    private EditText Name;
    private byte[] byteArray;
    private Bitmap bitmap_BAR_CODE;
    private int WRITE_EXST=1000;
    private EditText Address;
    private EditText Mobile;
    private TextView Dateofbirth;
    private TextView Dateofjourney;
    private TextView Transaction_Id;
    private EditText ID_No;
    private EditText Purpose;
    private TextView Scan_id;
    private Button   Payment;
    private ImageView Profile;
    private ImageButton DOBDate;
    private ImageButton DOJDate;
    private byte[] BARCODE_BYTE_ARRAY;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private TextView Application_status;
    private DatabaseReference ApplicationRef;
    private DatabaseReference User_app_ref;
    private DatabaseReference UNAVAILABLE_DATES;
    private StorageReference ApplicationStorageRef;
    //For PayPal Parts
    private String paymentAmount;
    private String state;
    private int flag;
    private Spinner spinner;
    private String id;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private AwesomeValidation mAwesomeValidation;
    public static int THE_TEST=0;
    private DatePicker datePicker;
    private static  final String[]paths = {"Passport", "Driving License", "Adhar Card","PAN"};
    private Calendar calendar;
    private int mDay, mMonth ,mYear;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            //Latter use when taking to production
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passdetails);
        THE_TEST=1;
        mAwesomeValidation = new AwesomeValidation(BASIC);




        DOBDate=(ImageButton)findViewById(R.id.DOBDate);
        DOJDate=(ImageButton)findViewById(R.id.DOJDate);
        calendar = Calendar.getInstance();
        mAuth=FirebaseAuth.getInstance();
        scan_id=(ImageView)findViewById(R.id.scan_pic) ;
        mDialog=new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        Name=(EditText)findViewById(R.id.name);
        Address=(EditText)findViewById(R.id.address);
        Mobile=(EditText)findViewById(R.id.mobile);
        Transaction_Id=(TextView)findViewById(R.id.transactionId);
        Transaction_Id.setText("Please copy the transaction id after payment keep it safe to query about your application later");
        Transaction_Id.setEnabled(false);
        ID_No=(EditText)findViewById(R.id.id_no);
        Dateofbirth=(TextView)findViewById(R.id.DOB);
        Dateofjourney=(TextView)findViewById(R.id.DOJ);
        Purpose=(EditText)findViewById(R.id.reason);
        Scan_id=(TextView)findViewById(R.id.scan);
        Profile=(ImageView) findViewById(R.id.profilephoto);
        Application_status=(TextView)findViewById(R.id.application_status);
        Payment=(Button)findViewById(R.id.payment);
        UNAVAILABLE_DATES=FirebaseDatabase.getInstance().getReference().child("UnavailableDates");
        UNAVAILABLE_DATES.keepSynced(true);
        ApplicationRef= FirebaseDatabase.getInstance().getReference().child("Applications");//Points to the root directory of the Database
        User_app_ref=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        ApplicationStorageRef= FirebaseStorage.getInstance().getReference();        //Points to the root directory of the Storage
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Passdetails.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //adding validation to edittexts
        mAwesomeValidation.addValidation(Passdetails.this, R.id.name,  "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.err_name);


        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mAwesomeValidation.validate())
                {

                }

            }
        });

      /*  try {
            bitmap_BAR_CODE = encodeAsBitmap("PAY-2RW02143UH910782FK7WXORI", BarcodeFormat.CODE_128, 600, 300);

            Profile.setImageBitmap(bitmap_BAR_CODE);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"barcode can't be generated",Toast.LENGTH_SHORT).show();
        } */


        DOJDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();

                final Calendar  m_three_months = Calendar.getInstance();

                m_three_months.add(Calendar.MONTH,2);
                mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                 mYear  = mcurrentDate.get(Calendar.YEAR);
                  mMonth = mcurrentDate.get(Calendar.MONTH);
                 mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Passdetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                      final  Calendar myCalendar = Calendar.getInstance();
                        //Calendar myCalenderCopy;
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                       // myCalenderCopy=myCalendar;

                        String myFormat = "dd-MM-yyyy"; //Change as you need
                    final    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);


                        UNAVAILABLE_DATES.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                Log.v("Mainas5",dataSnapshot.toString());


                                if(dataSnapshot.hasChild(sdf.format(myCalendar.getTime())))
                                {
                                    Toast.makeText(getApplicationContext(),"Selected date is unavailable",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Dateofjourney.setText(sdf.format(myCalendar.getTime()));
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });





                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(m_three_months.getTimeInMillis());
               // mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }


        });
        DOBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear  = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Passdetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        String myFormat = "dd-MM-yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        Dateofbirth.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        //PayPalService Call
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);



        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PROFILE_PHOTO);


            }
        });

        scan_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SCAN_ID);


            }
        });
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Names = Name.getText().toString().trim();
                final String Addresses = Address.getText().toString().trim();
                final String Mobiles = Mobile.getText().toString().trim();
                final String ID_NO = ID_No.getText().toString().trim();
                final String Purposes = Purpose.getText().toString().trim();
                final String DateOfBirth = Dateofbirth.getText().toString().trim();
                final String DateOfJourney = Dateofjourney.getText().toString().trim();


                if (byteArray == null) {
                    Toast.makeText(getApplicationContext(), "Please click a photo of yours", Toast.LENGTH_SHORT).show();


                } else
                {

                    if(mAwesomeValidation.validate())
                    {
                        if(!(TextUtils.isEmpty(Names) || TextUtils.isEmpty(Addresses) || TextUtils.isEmpty(DateOfJourney) || TextUtils.isEmpty(DateOfBirth) || TextUtils.isEmpty(Mobiles) || TextUtils.isEmpty(ID_NO) || TextUtils.isEmpty(Purposes) || TextUtils.isEmpty(byteArray.toString()) || TextUtils.isEmpty(scaniduri.toString()) ) )
                        {
                            UNAVAILABLE_DATES.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Log.v("Mainas5",dataSnapshot.toString());


                                    if(dataSnapshot.hasChild(DateOfJourney))
                                    {
                                        Toast.makeText(getApplicationContext(),"Selected date is unavailable",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        getPayment();
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // getPayment();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please fill the empty fields and upload your profile photo and Scan_id",Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                Log.v("Mainas3",DateOfJourney);
               // SubmitApplication();

            }
        });
    }
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        THE_TEST=0;
        super.onDestroy();
    }
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }


    private void getPayment() {
        //Getting the amount from editText
        paymentAmount = "9.00";

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Pass Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }



    private void SubmitApplication() {


        try {
            Profile.setImageBitmap(bitmap_BAR_CODE);
        }
        catch (Exception e)
        {
                Toast.makeText(getApplicationContext(),"Can't be generated",Toast.LENGTH_SHORT).show();
        }



        final String Names=Name.getText().toString().trim();
        final String Addresses= Address.getText().toString().trim();
        final String Mobiles= Mobile.getText().toString().trim();
        final String ID_NO= ID_No.getText().toString().trim();
        final String Purposes= Purpose.getText().toString().trim();
        final String DateOfBirth=Dateofbirth.getText().toString().trim();
        final String DateOfJourney=Dateofjourney.getText().toString().trim();


    /*    Log.v("TAG",Names);
        Log.v("TAG",Addresses);
        Log.v("TAG",Mobiles);
        Log.v("TAG",ID_NO);*/
        Log.v("TAG",Purposes);
//        Log.v("TAG profile",profilephoto.toString());
        Log.v("ScanUID",scaniduri.toString());


            mDialog.setMessage("Submitting your form");
            mDialog.show();
            final DatabaseReference newapplication=ApplicationRef.child(id);                           //Has the addre4ss of the new application
            final StorageReference newapplicationstorage=ApplicationStorageRef.child("Application");//Has the ref to Application directory

            //The applicant photo is added to the database

            newapplicationstorage.child(newapplication.getRef().toString()).child("barcode").putBytes(BARCODE_BYTE_ARRAY).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotBarcode) {

                    newapplicationstorage.child(newapplication.getRef().toString()).child("Profilephoto").putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotprofilepic) {

                            //The applicant Scanid is to be uploaded

                            newapplicationstorage.child(newapplication.getRef().toString()).child("Scan_Id").putFile(scaniduri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshotScanid) {


                            /*Adding the childs and their respective values of the application*/
                                    newapplication.child("Name").setValue(Names);
                                    newapplication.child("Address").setValue(Addresses);
                                    newapplication.child("Mobile").setValue(Mobiles);
                                    newapplication.child("ID_No").setValue(ID_NO);
                                    newapplication.child("Purpose").setValue(Purposes);
                                    newapplication.child("ApplicantPhoto").setValue(taskSnapshotprofilepic.getDownloadUrl().toString());
                                    newapplication.child("ApplicantScanId").setValue(taskSnapshotScanid.getDownloadUrl().toString());
                                    newapplication.child("Barcode_Image").setValue(taskSnapshotBarcode.getDownloadUrl().toString());
                                    newapplication.child("Uid").setValue(mAuth.getCurrentUser().getUid());
                                    newapplication.child("ApplicationStatus").setValue("Payment Received");
                                    newapplication.child("DateOfBirth").setValue(DateOfBirth);
                                    newapplication.child("DateOfJourney").setValue(DateOfJourney);


                                    Application_status.setText("Payment state :"+"Payment Received processing"+"\n" +"Transaction Id \n" +id);

                                    //A copy of the status is also stored in users own database
                                    User_app_ref.child("Applications").child(id).setValue(state);

                                    mDialog.dismiss();

                                    Intent notify = new Intent();

                                    notify.putExtra("Values", "Form submitted pass_no "+id);

                                    notify.setAction("Pas_with_some_value_has_changed");
                                    Log.v("Maina", "start6");
                                    sendBroadcast(notify);
                                    Log.v("Maina", "start5");
                                    Log.v("Maina", "start7");

                                    Payment.setEnabled(false);
                                    Transaction_Id.setText(id);
                                    Transaction_Id.setEnabled(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    //Refund();


                                    Application_status.setText("Form submission unsuccessful please ensure net connection and try again");
                                    mDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Form submission not successfull"+e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Form submission not successfull"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==PROFILE_PHOTO)
        {
            //When Applicant photo is selected
            if(requestCode==PROFILE_PHOTO)
            {
                profilephoto=data.getData();

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();


                Glide.with(Passdetails.this)
                        .load(byteArray)
                        .into(Profile);

               // Profile.setImageURI(profilephoto);

            }
        }
        else if(resultCode==RESULT_OK && requestCode==SCAN_ID)
        {
            //when applicant scanned user id is selected
            if(requestCode==SCAN_ID)
            {
                scaniduri=data.getData();

                Glide.with(Passdetails.this)
                        .load(scaniduri)
                        .into(scan_id);


               // scan_id.setImageURI(scaniduri);
            }

        }
        else  if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                Log.v("Here",state +" here i am confirm"+confirm.toJSONObject().toString());

                //if confirmation is not null
                if (confirm != null) {
                    try {

                        Log.v("Here",state +" here i am in try");
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.v("paymentExample", paymentDetails);

                        /*
                            "response": {
                                           "state": "approved",
                                           "id": "PAY-2RW02143UH910782FK7WXORI",
                                           "create_time": "2016-09-29T20:19:28Z",
                                           "intent": "sale"
                                        }
                         */



                        JSONObject object=confirm.toJSONObject();
                        JSONObject response=object.getJSONObject("response");
                        state=response.getString("state");
                        id=response.getString("id");



                        // bitmap_BAR_CODE = encodeAsBitmap(id, BarcodeFormat.CODE_128, 600, 300);  -->Originally
                        bitmap_BAR_CODE = encodeAsBitmap(id, BarcodeFormat.CODE_128, 1000, 300);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap_BAR_CODE.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        BARCODE_BYTE_ARRAY = stream.toByteArray();





                        Log.v("Here",state);

                        if(state.equals("approved"))
                        {
                            Log.v("Here",state +" here i am ");
                                SubmitApplication();
                        }
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Unable to generate barcode image",Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }

}
