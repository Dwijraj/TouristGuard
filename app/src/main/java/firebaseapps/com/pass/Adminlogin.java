package firebaseapps.com.pass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Adminlogin extends AppCompatActivity {

    private EditText AdminId;
    private Button loginadmin;
    private DatabaseReference mDatabaseref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        AdminId=(EditText)findViewById(R.id.adminid);
        loginadmin=(Button)findViewById(R.id.loginasadmin);
        mDatabaseref2= FirebaseDatabase.getInstance().getReference().child("Admin");

        loginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String admin_id=AdminId.getText().toString();

                if(!admin_id.isEmpty())
                {
                    mDatabaseref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(admin_id))
                            {
                                Intent adminpage=new Intent(Adminlogin.this,AdminPage.class);
                                adminpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(adminpage);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Enter correct admin_id",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter a admin_id",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
