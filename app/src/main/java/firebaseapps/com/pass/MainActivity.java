package firebaseapps.com.pass;


//PayPal email dwijrajbhattacharyya@gmail.com
//Password Maina123
//Tourist
//Tourist123

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import mohitbadwal.rxconnect.RxConnect;

public class MainActivity extends AppCompatActivity {



    private LinearLayout OTPIDS;
    private EditText OTPS;
    private EditText Phone;
    private EditText EMAILID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Button buttons;
    private final int GALLERY_OPEN=90;
    static final Integer WRITE_EXST = 0x2;
    private final Integer CAMERA = 0x4;//?user=SACHIN&key=d4c5c9993fXX&mobile=918093679890&message=(test sms)&senderid=INFOSM&accusage=1";
    //TelephonyManager Object to help fetch IMEI of the mobile
    private ProgressDialog prog;
    private DatabaseReference mDatabaseref;
    private DatabaseReference OTPDatabase;
    private int OTPint;
    private String OTPstring;
    private Random random;
    private RxConnect rxConnect;
    private int SEND_STATUS=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EMAILID=(EditText) findViewById(R.id.EMAIL);
        OTPIDS=(LinearLayout) findViewById(R.id.OTPID);
        rxConnect=new RxConnect(MainActivity.this);
        rxConnect.setCachingEnabled(false);
        OTPDatabase=FirebaseDatabase.getInstance().getReference().child("OTP");
        OTPS=(EditText)findViewById(R.id.OTP);
        Phone=(EditText)findViewById(R.id.editText3);
        prog=new ProgressDialog(this);
        buttons=(Button)findViewById(R.id.button);
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Users");//Points to the Users child  of the root parent


        // telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  //Telephony manager object is initiated
        mAuth=FirebaseAuth.getInstance();                           //Firebase Auth instance
        mAuthlistener=new FirebaseAuth.AuthStateListener() {        //Firebase Auth Listener that checks if the user if logged in
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Log.v("Maina","start -1");
                    /* If the user is logged in takes user to the apply pass activity*/
                    Intent Apply=new Intent(MainActivity.this,ApplyPass.class);
                    Apply.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Apply);
                }


            }
        };

        if(Build.VERSION.SDK_INT>=23)
        {

            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,GALLERY_OPEN);


        }



        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.v("ERROR","1");
                if ((Phone.getText().toString().length()==10)&& !TextUtils.isEmpty(EMAILID.getText().toString().trim())
                        &&EMAILID.getText().toString().trim().contains("@")) {            //makes sure that user enter his/her phone number

                    Log.v("ERROR","2");
                    prog.setMessage("Signing you..");

                    final String IMEI = Phone.getText().toString().trim();
                    //  final String IMEI = "455567888344432";
                    final String EMAIL = IMEI + "@" + IMEI + ".com";
                    final String PASSWORD = IMEI;
                    Random rn = new Random();
                    int n = 999 - 99;
                    int i = rn.nextInt() % n;
                    if(i<0)
                    {
                        i*=-1;
                    }
                    OTPint=  99 + i;
                    OTPstring=String.valueOf(OTPint);
//                    Log.v("OTP",OTPstring);

                    Toast.makeText(getApplicationContext(),"YOUR OTP IS "+OTPstring,Toast.LENGTH_SHORT ).show();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            GMailSender sender = new GMailSender(Constants.EMAIL_SENDER
                                    , Constants.EMAIL_PASSWORD_SENDER);
                            try {
                                sender.sendMail("OTP from Pass", "One time password is " +
                                                OTPstring, Constants.EMAIL_SENDER,
                                        EMAILID.getText().toString().trim());
                            } catch (Exception e) {

                                 Log.e("ERROR", e.getMessage(), e);
                                SEND_STATUS=1;

                            }

                        }
                    }).start();


                            /*     // if(SEND_STATUS==1)
                                 // {

                                      SendMail sm = new SendMail(MainActivity.this,EMAILID.getText().toString().trim(),"OTP from Pass","One time password is" + OTPstring);
                                      sm.execute();

                                 // } */



                   buttons.setText("GET STARTED");
                    OTPS.setVisibility(View.VISIBLE);
                    Phone.setEnabled(false);
                    OTPIDS.setVisibility(View.VISIBLE);


                  /*  new Thread(new Runnable() {
                        @Override
                        public void run() {

                            rxConnect.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
                            rxConnect.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
                            rxConnect.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+IMEI);
                            rxConnect.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"Your OTP is "+OTPstring);
                            rxConnect.setParam(Constants.SMS_PARAM_KEY_SENDERID,"INFOSM");
                            rxConnect.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,"2");
                            rxConnect.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {


                                @Override
                                public void onResult(String result) {
                                    //do something on result


                                    Toast.makeText(getApplicationContext(),"OTP sent ",Toast.LENGTH_SHORT).show();

                                    buttons.setText("GET STARTED");
                                    OTPS.setVisibility(View.VISIBLE);
                                    Phone.setEnabled(false);
                                    OTPIDS.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNoResult() {
                                    //do something

                                    Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    //do somenthing on error

                                    Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();


                                }


                            });


                        }
                    }).start(); */
                    buttons.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final  String   OTP_INPUT=OTPS.getText().toString().trim();
                            if(OTP_INPUT.equals(OTPstring))
                            {

                                prog.show();

                                mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {


                                        prog.dismiss();
                                        Toast.makeText(getApplicationContext(), "ACCOUNT CREATED", Toast.LENGTH_SHORT).show();


                                        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {

                                                Log.v("TAG1", "CREATED AND SIGNED IN");

                                                DatabaseReference particular_user = mDatabaseref.child(mAuth.getCurrentUser().getUid());
                                                particular_user.child("IMEI").setValue(IMEI);
                                                particular_user.child("Contact").setValue(Phone.getText().toString());

                                                prog.dismiss();


                                                Toast.makeText(getApplicationContext(), "Signed in successful", Toast.LENGTH_SHORT).show();

                                                Intent Apply = new Intent(MainActivity.this, ApplyPass.class);
                                                Apply.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(Apply);


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {


                                                prog.dismiss();

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {


                                                prog.dismiss();

                                                Toast.makeText(getApplicationContext(), "Signed in successful", Toast.LENGTH_SHORT).show();

                                                Intent Apply = new Intent(MainActivity.this, ApplyPass.class);
                                                Apply.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(Apply);


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                prog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Couldn't sign you up please check internet settings", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                });




                            }
                            else if( OTPS.getVisibility()== View.INVISIBLE)
                            {

                                Toast.makeText(getApplicationContext(),"OTP couldn't be sent ensure internet connection",Toast.LENGTH_SHORT).show();

                            }
                            else if(OTP_INPUT.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"Enter the correct OTP",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"OTP doesnot match",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Phone number...",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 90:
                askForPermission(android.Manifest.permission.CAMERA,CAMERA);
                break;

            case 0x4:

                break;
        }


    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {


                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }


    }


}