package firebaseapps.com.pass;


//PayPal email dwijrajbhattacharyya@gmail.com
//Password Maina123
import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText Phone;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Button buttons;
    private final int GALLERY_OPEN=90;
    static final Integer WRITE_EXST = 0x2;
    private final Integer CAMERA = 0x4;
    private TelephonyManager telephonyManager;                          //TelephonyManager Object to help fetch IMEI of the mobile
     private ProgressDialog prog;
    private DatabaseReference mDatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


                if (!Phone.getText().toString().isEmpty()) {            //makes sure that user enter his/her phone number
                    prog.setMessage("Signing you..");

                    Log.v("HERE", "WHERE");

                    final String IMEI = Phone.getText().toString();
                  //  final String IMEI = "455567888344432";
                    final String EMAIL = IMEI + "@" + IMEI + ".com";
                    final String PASSWORD = IMEI;

                    //   Toast.makeText(getApplicationContext(),IMEI,Toast.LENGTH_SHORT).show();

                    Log.v("HERE", IMEI);

                    prog.show();

                    mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {


                            prog.dismiss();
                            Toast.makeText(getApplicationContext(), "ACCOUNT CREATED", Toast.LENGTH_SHORT).show();
                        /*On successfully creating the account the user is prompted to the apply pass activity*/

                            mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Log.v("TAG1", "CREATED AND SIGNED IN");

                                    DatabaseReference particular_user = mDatabaseref.child(mAuth.getCurrentUser().getUid());
                                    particular_user.child("IMEI").setValue(IMEI);
                                    particular_user.child("Contact").setValue(Phone.getText().toString());

                                    prog.dismiss();
                                /*On successfully signing into the account the user is prompted to the apply pass activity*/

                                    Toast.makeText(getApplicationContext(), "Signed in successful", Toast.LENGTH_SHORT).show();

                                    Intent Apply = new Intent(MainActivity.this, ApplyPass.class);
                                    Apply.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(Apply);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.v("TAG1", "CREATED CAN'T SIGNED IN");

                                    prog.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Toast.makeText(getApplicationContext(),"Account can't be created checking if user exists..",Toast.LENGTH_SHORT).show();

                            mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {


                                    prog.dismiss();
                                /*On successfully signing into the account the user is prompted to the apply pass activity*/
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
        } else {

           // Toast.makeText(getApplicationContext(),"Storage and camera permissions are already enabled",Toast.LENGTH_SHORT).show();
        }


    }


}