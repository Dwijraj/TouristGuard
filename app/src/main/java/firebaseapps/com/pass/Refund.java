package firebaseapps.com.pass;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Refund extends AppCompatActivity {

    private EditText tras_id;
    private TextView View;
    private Button check;
    private  DatabaseReference databaseReferenceRefund;
    private DatabaseReference mDatabaseref;
    private DatabaseReference MAIN_ROOT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);


        Passdetails.THE_TEST=1;
        databaseReferenceRefund =FirebaseDatabase.getInstance().getReference().child("RefundRequests");


        MAIN_ROOT=FirebaseDatabase.getInstance().getReference();
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");  //Reference to applications
        tras_id=(EditText)findViewById(R.id.viewtransactionid);
        View=(TextView)findViewById(R.id.viewstatus);
        check=(Button)findViewById(R.id.viewcheck);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String pass=tras_id.getText().toString().trim();

                        databaseReferenceRefund.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(pass))
                                {
                                    databaseReferenceRefund.child(pass).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshots) {
                                            View.setText(dataSnapshots.getValue(String.class));
                                            //View.setText("Already Exists");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else
                                {
                                   // View.setText("Reviewing if Refund possible");
                                    mDatabaseref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(pass))
                                            {
                                                mDatabaseref.child(pass).child("ApplicationStatus").setValue("Reviewing if Refund possible").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        MAIN_ROOT.child("VerifiedUsers").child(pass).removeValue();
                                                        MAIN_ROOT.child("VehicleBooking").child(pass).removeValue();

                                                       MAIN_ROOT.child("Applications").child(pass).child("Uid").addValueEventListener(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(DataSnapshot dataSnapshot) {

                                                               String UID=dataSnapshot.getValue(String.class);

                                                               MAIN_ROOT.child("Users").child(UID).child("Applications").child(pass).setValue("Refund requested");

                                                           }

                                                           @Override
                                                           public void onCancelled(DatabaseError databaseError) {

                                                           }
                                                       });
                                                        DatabaseReference databaseReferenceRefund =FirebaseDatabase.getInstance().getReference().child("RefundRequests");

                                                        databaseReferenceRefund.child(pass).setValue("Checking in progress..").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                View.setText("Checking in progress..");
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                            else
                                            {
                                                View.setText("No such application registered");
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });



                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });






            }
        });

    }
}
