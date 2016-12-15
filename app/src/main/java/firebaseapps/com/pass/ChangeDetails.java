package firebaseapps.com.pass;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChangeDetails extends AppCompatActivity {

    private EditText Passno;
    private EditText Idno;
    private TextView DOJ;
    private ImageButton img_button;
    private FirebaseAuth mAuth;
    private Button update;
    private DatabaseReference mDatabaseref;
    private DatabaseReference  UNAVAILABLE_DATES;
    private DatabaseReference databasereferenceforchange;
    private String passno;
    private String state;
    private int mDay, mMonth ,mYear;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private Intent intent;
    private int N=1;
    private DateFormat format;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        N=400;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);

        state="Payment_Pending";
        Passdetails.THE_TEST=1;
        UNAVAILABLE_DATES=FirebaseDatabase.getInstance().getReference().child("UnavailableDates");
        UNAVAILABLE_DATES.keepSynced(true);

        mAuth=FirebaseAuth.getInstance();
        img_button=(ImageButton)findViewById(R.id.image_Button);
        Passno=(EditText)findViewById(R.id.change_pass_number);
        Idno=(EditText)findViewById(R.id.change_idno);
        DOJ=(TextView)findViewById(R.id.change_DOJ);
        update=(Button)findViewById(R.id.change_button);
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");
        databasereferenceforchange=FirebaseDatabase.getInstance().getReference();

        intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Passno.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter The pass number first", Toast.LENGTH_SHORT).show();
                } else {

                    final String tr_id=Passno.getText().toString().trim();
                    mDatabaseref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(tr_id))
                            {
                                //Toast.makeText(getApplicationContext(),"Pass exists",Toast.LENGTH_SHORT).show();
                                mDatabaseref.child(tr_id).child("DateOfJourney").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String dates=dataSnapshot.getValue(String.class);
                                        //DateFormat
                                                format = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
                                        try {

                                            Date date_from_database = format.parse(dates);



                                          //  Toast.makeText(getApplicationContext(),"Successful"+date_from_database.toString(),Toast.LENGTH_SHORT).show();


                                            //Fri Dec 16 00:00:00 IST 2016

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date_from_database);
                                            calendar.add(Calendar.DATE, -5);


                                            Date date_today_from_android=new Date(System.currentTimeMillis());
                                            String Three_Days_Ago = format.format(calendar.getTime());
                                            String Todays_date=format.format(date_today_from_android);

                                            Date date_from_three_days = format.parse(Three_Days_Ago);
                                            Date Today=format.parse(Todays_date);


                                            // Actual_date Tue Dec 13 00:00:00 IST 2016
                                            // todays_date Mon Dec 12 00:00:00 IST 2016

                                            boolean Check=Today.before(date_from_three_days);

                                            if(Check || Today.equals(date_from_three_days))
                                            {

                                                final Calendar mcurrentDate = Calendar.getInstance();

                                                final Calendar  m_three_months = Calendar.getInstance();

                                                m_three_months.add(Calendar.MONTH,2);
                                                mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                                                mYear  = mcurrentDate.get(Calendar.YEAR);
                                                mMonth = mcurrentDate.get(Calendar.MONTH);
                                                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                                                DatePickerDialog mDatePicker = new DatePickerDialog(ChangeDetails.this, new DatePickerDialog.OnDateSetListener() {
                                                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                                        final  Calendar myCalendar = Calendar.getInstance();
                                                        //Calendar myCalenderCopy;
                                                        myCalendar.set(Calendar.YEAR, selectedyear);
                                                        myCalendar.set(Calendar.MONTH, selectedmonth);
                                                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                                                        // myCalenderCopy=myCalendar;

                                                        String myFormat = "dd-MM-yyyy"; //Change as you need
                                                        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                                                        UNAVAILABLE_DATES.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                                if(dataSnapshot.hasChild(sdf.format(myCalendar.getTime())))
                                                                {
                                                                    Toast.makeText(getApplicationContext(),"Selected date is unavailable",Toast.LENGTH_SHORT).show();
                                                                }
                                                                else
                                                                {
                                                                    DOJ.setText(sdf.format(myCalendar.getTime()));
                                                                }

                                                            }
                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });





                                                       // DOJ.setText(sdf.format(myCalendar.getTime()));




                                                        mDay = selectedday;
                                                        mMonth = selectedmonth;
                                                        mYear = selectedyear;
                                                    }
                                                }, mYear, mMonth, mDay);
                                                mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                                                mDatePicker.getDatePicker().setMaxDate(m_three_months.getTimeInMillis());
                                                // mDatePicker.setTitle("Select date");
                                                if(!state.equals("approved"))
                                                {
                                                        mDatePicker.show();
                                                }
                                               // mDatePicker.show();

                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"Sorry No changes can be made",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (Exception ex)
                                        {

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            }
                            else
                            {
                                //m2Dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No such application found",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

           }




            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String tr_id=Passno.getText().toString().trim();
                final String id_no=Idno.getText().toString().trim();
                passno=tr_id;

                mDatabaseref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(tr_id))
                        {
                            DatabaseReference idref=mDatabaseref.child(tr_id).child("ID_No");

                            idref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String id_nos=dataSnapshot.getValue(String.class);

                                    if(id_nos.equals(id_no)&&(!DOJ.getText().toString().isEmpty()))
                                    {
                                        if(N==1) {
                                            getPayments();
                                            N=99;
                                        }
                                    }
                                    else {
                                        if (DOJ.getText().toString().isEmpty()) {
                                            Toast.makeText(getApplicationContext(), "Select a proper Date", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Wrong Id no", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            //m2Dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"No such application found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }
    private void getPayments() {
        //Getting the amount from editText
        String  paymentAmount = "1.00";

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Pass Change Fee",
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {

                        JSONObject object=confirm.toJSONObject();
                        JSONObject response=object.getJSONObject("response");
                        state=response.getString("state");
                         if(state.equals("approved"))
                            {
                           DatabaseReference changeDate=mDatabaseref.child(passno).child("DateOfJourney");      //Reference to Date of Journey
                           DatabaseReference ChangeDate=mDatabaseref.child(passno).child("Uid");
                            String DOJ_new=DOJ.getText().toString().trim();             //Gets the new DOJ

                            ChangeDate.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshotDE) {

                                    String Uid=dataSnapshotDE.getValue(String.class);

                                    databasereferenceforchange.child("Users").child(Uid).child("Applications").child(passno).setValue("Payment Received from Refund");




                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mDatabaseref.child(passno).child("ApplicationStatus").setValue("Payment Received from Refund");
                            changeDate.setValue(DOJ_new).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {     //Sets the new value in Database


                                    Toast.makeText(getApplicationContext(),"Changes made successfully",Toast.LENGTH_LONG).show();

                                    databasereferenceforchange.child("VerifiedUsers").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(passno))
                                            {
                                                databasereferenceforchange.child("VerifiedUsers").child(passno).removeValue();
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    databasereferenceforchange.child("VehicleBooking").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(passno))
                                            {

                                                databasereferenceforchange.child("VehicleBooking").child(passno).removeValue();
                                                finish();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                   // finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }
                    } catch (JSONException e) {

                    }
                }
            }
        }

    }
}
