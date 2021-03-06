package firebaseapps.com.pass;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewPass extends AppCompatActivity {

    private ImageView scan_id2;
    private TextView Name2;
    private TextView Address2;
    private Button Vehicle;
    private Button generatebarcode;
    private TextView Mobile2;
    private TextView Dateofbirth2;
    private TextView Dateofjourney2;
    private TextView Transaction_Id2;
    private TextView ID_No2;
    private TextView Purpose2;
    private TextView Scan_id2;
    private TextView DriverName;
    private TextView CarNumber;
    private ImageView Profile2;
    private  String pass;
    private TextView Application_status2;
    private DatabaseReference ApplicationRef2;
    private  Application app;
    private TextView ID_source;
    private TextView Gate;
    private TextView place_of_visit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Passdetails.THE_TEST==1)
            finish();
        setContentView(R.layout.pass_history);

        place_of_visit=(TextView) findViewById(R.id.place);
        Gate=(TextView) findViewById(R.id.GATE);
        CarNumber=(TextView)findViewById(R.id.Carnumbers);
        DriverName=(TextView)findViewById(R.id.DriverNAME);
      final  ImageView imageView=(ImageView) findViewById(R.id.BAR_CODE_SHOW);
       final TextView  Pass=(TextView)findViewById(R.id.PassNumber);
        generatebarcode=(Button)findViewById(R.id.Display_Bar_Code);
        Vehicle=(Button)findViewById(R.id.vehicle);
        scan_id2=(ImageView)findViewById(R.id.SCAN_PIC) ;
        Name2=(TextView)findViewById(R.id.SCAN_NAME);
        Address2=(TextView)findViewById(R.id.SCAN_ADDRESS);
        Mobile2=(TextView)findViewById(R.id.SCAN_MOBILE);
        ID_No2=(TextView)findViewById(R.id.SCAN_ID);
        Dateofbirth2=(TextView)findViewById(R.id.SCAN_DOB);
        Dateofjourney2=(TextView)findViewById(R.id.SCAN_DOJ);
        Purpose2=(TextView)findViewById(R.id.SCAN_REASON);
        ID_source=(TextView)findViewById(R.id.ID_Source_field);
        Profile2=(ImageView)findViewById(R.id.SCAN_PROFILE);
        Application_status2=(TextView)findViewById(R.id.SCAN_STATUS);
        ApplicationRef2= FirebaseDatabase.getInstance().getReference().child("Applications");//Points to the root directory of the Database

        Intent i=getIntent();
        Bundle extras=i.getExtras();
         pass=extras.getString("Pass");




        Vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                Intent TO_VEHICLES=new Intent(ViewPass.this,Vehicles.class);
                TO_VEHICLES.putExtra("BookedBy",pass);
                startActivity(TO_VEHICLES);
            }
        });

        ApplicationRef2.child(pass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                app=dataSnapshot.getValue(Application.class);  //App crasehs due to this line

                Name2.setText(app.Name);
                Address2.setText(app.Address);
                Mobile2.setText(app.Mobile);
                ID_No2.setText(app.ID_No);
                Dateofbirth2.setText(app.DateOfBirth);
                Dateofjourney2.setText(app.DateOfJourney);
                Purpose2.setText(app.Purpose);
                Application_status2.setText(app.ApplicationStatus.toUpperCase());
                ID_source.setText(app.ID_Source);
                CarNumber.setText(app.Carnumber);
                DriverName.setText(app.Drivername);
                Gate.setText(app.Gate);
                place_of_visit.setText(app.Destination);

                if(app.ApplicationStatus.contains("Verified"))
                {

                    Vehicle.setEnabled(true);


                }


                Glide.with(getApplicationContext())
                        .load(app.ApplicantPhoto)
                        .into(Profile2);


                Glide.with(getApplicationContext())
                        .load(app.ApplicantScanId)
                        .into(scan_id2);

                Glide.with(getApplicationContext())
                        .load(app.Barcode_Image)
                        .into(imageView);

                Pass.setText(pass);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        generatebarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NEW_INTENT=new Intent(ViewPass.this,Barcode_Display.class);
                NEW_INTENT.putExtra("Barcode_URL",app.Barcode_Image);
                NEW_INTENT.putExtra("Pass_No",pass);
                startActivity(NEW_INTENT);

            }
        });







    }
}
