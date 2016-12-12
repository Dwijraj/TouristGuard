package firebaseapps.com.pass;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ApplyPass extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private DatabaseReference mDatabaseref;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Button applypass;
    private Button checkpassstatus;
    private Button Loginasadmin;
    private Button ViewPass;
    private String user;
    private Button changepassdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_pass);

        Log.v("Maina","start 0");
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");      //Reference to where the applications are stored
        applypass=(Button)findViewById(R.id.getstarted);                                        //To apply for new pass
        checkpassstatus=(Button)findViewById(R.id.checkpassstatus);                             //To check pass status
        Loginasadmin=(Button)findViewById(R.id.loginasadmin);                                   //To login as admin
        changepassdetails=(Button)findViewById(R.id.Changepassdetails);                         //To change the details of the pass
        ViewPass=(Button)findViewById(R.id.Viewpass);                                           //To view the pass


        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());       //Gets the network status

        /**If network is enable but the service is not running
        * The service is not running
         * the system will start the service
         * */
        if(status.equals("Wifi enabled") || status.equals("Mobile data enabled"))
        {
           if(!isMyServiceRunning(MyService.class)    /*Checks if service is ruuning */ )
            {
                Intent serviceIntent = new Intent(ApplyPass.this, MyService.class);
                startService(serviceIntent);                                                //starts the service if it is not running
            }
        }


        mAuth=FirebaseAuth.getInstance();               //Gets the firebase auth object

        mAuthlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        if(firebaseAuth.getCurrentUser()==null) //Checks if the user is logged in
                        {

                    /* If the user is not  logged in takes Allows user to register first*/
                                Intent Apply=new Intent(ApplyPass.this,MainActivity.class);
                                Apply.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(Apply);

                        }
                else
                        {
                            user=mAuth.getCurrentUser().getUid();       //Gets the Unique user ID UID
                        }
            }
        };





     /*   Loginasadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_admin=new Intent(ApplyPass.this,Adminlogin.class);     //Login as admin
                startActivity(login_admin);
            }
        }); */

        applypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Takes user to the new activity on applyling pass
                Intent Passdetail=new Intent(ApplyPass.this,Passdetails.class);     //Allows user to fill up a new pass application
                startActivity(Passdetail);
            }
        });

       checkpassstatus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent check=new Intent(ApplyPass.this,CheckPassDetails.class);      //Allows user to check pass details
               startActivity(check);
           }
       });
        changepassdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                         new AlertDialog.Builder(ApplyPass.this)
                        .setTitle("Select appropriate option")
                        .setMessage("Select appropriate option")
                        .setPositiveButton("Change Date of Journey", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent ChangeDetails=new Intent(ApplyPass.this,ChangeDetails.class);    //Allows user to change the DOJ
                                startActivity(ChangeDetails);
                            }
                        })
                        .setNegativeButton("Request refund", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent In=new Intent(new Intent(ApplyPass.this,Refund.class));
                                startActivity(In);
                                  // do nothing
                            }
                        })
                        .setIcon(R.mipmap.pass)
                        .show();

            }
        });

        ViewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent view_pass=new Intent(ApplyPass.this,View_Pass.class);            //Allows user to view a particular pass
                startActivity(view_pass);
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthlistener);                                      //Auth listener to check if user is present

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {                         //Checks if MyService is running
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
