package firebaseapps.com.pass;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckPassDetails extends AppCompatActivity {

    private EditText tras_id;
    private TextView app_status;
    private Button check;
    private ProgressDialog m2Dialog;
    private DatabaseReference mDatabaseref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass_details);

        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");  //Reference to applications
        tras_id=(EditText)findViewById(R.id.viewtransactionid);
        app_status=(TextView)findViewById(R.id.viewstatus);
        check=(Button)findViewById(R.id.viewcheck);
        m2Dialog=new ProgressDialog(this);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m2Dialog.setMessage("Checking if application  exists and fetching the status");
                m2Dialog.show();

               final String tr_id=tras_id.getText().toString();

                mDatabaseref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(tr_id))
                        {
                            DatabaseReference statusref=mDatabaseref.child(tr_id).child("ApplicationStatus");

                            statusref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    m2Dialog.dismiss();
                                    String appli_status=dataSnapshot.getValue(String.class);
                                    app_status.setText("Application status");

                                    app_status.append(" :"+appli_status.toUpperCase());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            //When application is not present
                            m2Dialog.dismiss();
                            app_status.setText("Application status");

                            app_status.append(" :"+"APPLICATION NOT FOUND");
                            Toast.makeText(getApplicationContext(),"no such application found",Toast.LENGTH_LONG).show();
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
