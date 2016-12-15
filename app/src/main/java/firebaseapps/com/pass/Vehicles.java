package firebaseapps.com.pass;


import android.app.Dialog;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Vehicles extends AppCompatActivity {

    private ImageView  DriverLicense;
    private ImageView  Insurance;
    private ImageView Documents;
    private Uri DRIVER_LICENSE_URI;
    private Uri INSURANCE_URI;
    private Uri DOCUMENTS_URI;
    private EditText Drivername;
    private String PASS_WHICH_BOOKED;
    private Dialog NUMBER_OF_PASSENGER;
    private Dialog PASSENGER_NAME;
    private EditText Operatorname;
    private EditText Pss1;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass1);
    private EditText Pss2;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass2);
    private EditText Pss3;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass3);
    private EditText Pss4;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass4);
    private EditText Pss5;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass5);
    private EditText Pss6;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass6);
    private EditText Pss7;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass7);
    private EditText Pss8;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass8);
    private EditText Pss9;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass9);
    private EditText Pss10;//=(EditText)PASSENGER_NAME.findViewById(R.id.pass10);
    private String Passenger1;
    private String Passenger2;
    private String Passenger3;
    private String Passenger4;
    private String Passenger5;
    private String Passenger6;// = null;
    private String Passenger7;
    private String Passenger8;
    private String Passenger9;
    private String Passenger10;
    private int Selected;
    private final int DRIVER_LICENSE_REQUEST_CODE=343;       //To recognise in onActivityresult
    private final int INSURANCE_REQUEST_CODE=434;
    private final int DOCUMENTS_REQUEST_CODE=535;
    private DatabaseReference APPLICATION_REF;
    private DatabaseReference databaseReferenceVerified;
    private DatabaseReference VEHICLE_DATABASE_REF;
    private StorageReference VEHICLE_STORAGE_REF;
    private Button Submit;
    private String DRIVER_NAME_STRING;
    private String OPERATOR_NAME_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);


        Intent FROM_VIEW_PASS=getIntent();
        Bundle extras=FROM_VIEW_PASS.getExtras();
        PASS_WHICH_BOOKED=extras.getString("BookedBy");


        NUMBER_OF_PASSENGER=new Dialog(this);
        PASSENGER_NAME=new Dialog(this);
        PASSENGER_NAME.setCanceledOnTouchOutside(false);
        databaseReferenceVerified=FirebaseDatabase.getInstance().getReference().child("VerifiedUsers");
        databaseReferenceVerified.keepSynced(true);
        VEHICLE_STORAGE_REF= FirebaseStorage.getInstance().getReference().child("VehiclesBooking");
        VEHICLE_DATABASE_REF= FirebaseDatabase.getInstance().getReference().child("VehicleBooking");
        APPLICATION_REF=FirebaseDatabase.getInstance().getReference();//.child("Applications");
        Drivername=(EditText)findViewById(R.id.driversname);
        Operatorname=(EditText)findViewById(R.id.operatore_name);
        DriverLicense = (ImageView) findViewById(R.id.driverlicense);
        Insurance = (ImageView) findViewById(R.id.insurance);
        Documents = (ImageView) findViewById(R.id.documents);
        Submit = (Button) findViewById(R.id.submit);

        Insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, INSURANCE_REQUEST_CODE);
            }
        });

        Documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, DOCUMENTS_REQUEST_CODE);
            }
        });

        DriverLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, DRIVER_LICENSE_REQUEST_CODE);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                try
                {

                   // final String
                            DRIVER_NAME_STRING=Drivername.getText().toString().trim();
                   // final String
                            OPERATOR_NAME_STRING=Operatorname.getText().toString().trim();


                    NUMBER_OF_PASSENGER.setContentView(R.layout.numberofperson);
                    NUMBER_OF_PASSENGER.setTitle("Number of Passengers");
                  //  NUMBER_OF_PASSENGER.setCanceledOnTouchOutside(false);
                    NUMBER_OF_PASSENGER.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
                    NUMBER_OF_PASSENGER.show();


                   final RadioGroup radioGroup=(RadioGroup) NUMBER_OF_PASSENGER.findViewById(R.id.radiogroup);
                    Button Confirm=(Button) NUMBER_OF_PASSENGER.findViewById(R.id.Confirm_number_of_person);

                    Confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Selected=radioGroup.getCheckedRadioButtonId();

                            String Sel=Selected+"";

                            if(Sel.equals("2131427515"))
                            {
                                //Passes 5-10
                                NUMBER_OF_PASSENGER.dismiss();
                                PASSENGER_NAME.setContentView(R.layout.numberfivetoten);
                                PASSENGER_NAME.show();


                                Pss1=(EditText)PASSENGER_NAME.findViewById(R.id.pass1);
                                Pss2=(EditText)PASSENGER_NAME.findViewById(R.id.pass2);
                                Pss3=(EditText)PASSENGER_NAME.findViewById(R.id.pass3);
                                Pss4=(EditText)PASSENGER_NAME.findViewById(R.id.pass4);
                                Pss5=(EditText)PASSENGER_NAME.findViewById(R.id.pass5);
                                Pss6=(EditText)PASSENGER_NAME.findViewById(R.id.pass6);
                                Pss7=(EditText)PASSENGER_NAME.findViewById(R.id.pass7);
                                Pss8=(EditText)PASSENGER_NAME.findViewById(R.id.pass8);
                                Pss9=(EditText)PASSENGER_NAME.findViewById(R.id.pass9);
                                Pss10=(EditText)PASSENGER_NAME.findViewById(R.id.pass10);

                                Button Book=(Button) PASSENGER_NAME.findViewById(R.id.BOOK);

                                Book.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                                        if(TextUtils.isEmpty(Pss1.getText().toString())||TextUtils.isEmpty(Pss2.getText().toString())||TextUtils.isEmpty(Pss3.getText().toString())||TextUtils.isEmpty(Pss4.getText().toString())||TextUtils.isEmpty(Pss5.getText().toString()))
                                        {
                                            Toast.makeText(getApplicationContext(),"Select 2-4 or fill applicant names in order",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    BookVehicleFiveToTen();
                                                //    NotifyAllPassengers();

                                                }
                                            }).start();


                                        }


                                    }
                                });


                            }
                            else
                            {
                                //Passes 1-4
                                NUMBER_OF_PASSENGER.dismiss();
                                PASSENGER_NAME.setContentView(R.layout.numbertwotofour);
                                PASSENGER_NAME.show();


                                Pss1=(EditText)PASSENGER_NAME.findViewById(R.id.pass1);
                                Pss2=(EditText)PASSENGER_NAME.findViewById(R.id.pass2);
                                Pss3=(EditText)PASSENGER_NAME.findViewById(R.id.pass3);
                                Pss4=(EditText)PASSENGER_NAME.findViewById(R.id.pass4);


                                Button Book=(Button) PASSENGER_NAME.findViewById(R.id.BOOK);

                                Book.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                                        if(TextUtils.isEmpty(Pss1.getText().toString()))
                                        {
                                            Toast.makeText(getApplicationContext(),"Please fill applicant name",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    BookVehicleTwoToFour();
                                                   // NotifyAllPassengersOneToFour();

                                                }
                                            }).start();


                                        }


                                    }
                                });
                            }

                        }
                    });






                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"Make sure all fields are filled and images are uploaded",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void NotifyAllPassengersOneToFour() {

        if(!TextUtils.isEmpty(Pss1.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss1.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }


        if(!TextUtils.isEmpty(Pss2.getText().toString()))
        {

            final String ApplicationNo;
            ApplicationNo=Pss2.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("Applied for vehicle by user with pass"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss3.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss3.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss4.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerEight").setValue("N/A");



            final String ApplicationNo;
            ApplicationNo=Pss4.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }

    }

    private void BookVehicleTwoToFour() {

        PASSENGER_NAME.dismiss();
        Passenger1=Pss1.getText().toString().trim();

        try
        {
            if(TextUtils.isEmpty(Pss2.getText().toString()))
            {
                Passenger2="NA";
                Passenger3="NA";
                Passenger4="NA";


            }
            else if(TextUtils.isEmpty(Pss3.getText().toString()))
            {
                Passenger2=Pss2.getText().toString().trim();
                Passenger3="NA";//Pss7.getText().toString().trim();
                Passenger4="NA";

            }
            else if(TextUtils.isEmpty(Pss8.getText().toString()))
            {
                Passenger2=Pss2.getText().toString().trim();
                Passenger3=Pss7.getText().toString().trim();
                Passenger4="NA";//Pss8.getText().toString().trim();
            }
            else if(TextUtils.isEmpty(Pss9.getText().toString()))
            {

                Passenger2=Pss2.getText().toString().trim();
                Passenger3=Pss3.getText().toString().trim();
                Passenger4=Pss4.getText().toString().trim();

            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Make sure no empty field precedes a non-empty field",Toast.LENGTH_LONG).show();
        }




        databaseReferenceVerified.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if(dataSnapshot.hasChild(Passenger1)&&dataSnapshot.hasChild(Passenger2)&&dataSnapshot.hasChild(Passenger3)&&dataSnapshot.hasChild(Passenger4))
                {
                    FinalizeFormOneTwoFour();
                    NotifyAllPassengersOneToFour();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Make Sure all passes are verified",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FinalizeFormOneTwoFour() {


        // final ProgressDialog Upload =new ProgressDialog(getApplicationContext());
        VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("DriverLicense").putFile(DRIVER_LICENSE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotDRIVERLICENSE) {


                VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("Documents").putFile(DOCUMENTS_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotDOCUMENTS) {

                        VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("Insurance").putFile(INSURANCE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshotINSURANCE) {



                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("Documents_LINK").setValue(taskSnapshotDOCUMENTS.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("DriverLicense_LINK").setValue(taskSnapshotDRIVERLICENSE.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("Insurance_LINK").setValue(taskSnapshotINSURANCE.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("DriverName").setValue(DRIVER_NAME_STRING);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerOne").setValue(Passenger1);
                                if(TextUtils.isEmpty(Pss2.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTwo").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTwo").setValue(Pss2.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss3.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerThree").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerThree").setValue(Pss3.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss4.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerFour").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerFour").setValue(Pss4.getText().toString());
                                }
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerFive").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSix").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerEight").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerNine").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTen").setValue("N/A");
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("OperatorName").setValue(OPERATOR_NAME_STRING).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent notify = new Intent();

                                        notify.putExtra("Values", "Vehicle booked for pass_no "+PASS_WHICH_BOOKED);

                                        notify.setAction("Pas_with_some_value_has_changed");
                                         sendBroadcast(notify);



                                        //  Toast.makeText(getApplicationContext(),"Uploading Successful",Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                        Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();


                                    }
                                });




                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"Uploading Failed...",Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void BookVehicleFiveToTen() {

        PASSENGER_NAME.dismiss();
      /*  final String Passenger1;
        final String Passenger2;
        final String Passenger3;
        final String Passenger4;
        final String Passenger5;
        String Passenger6;// = null;
        String Passenger7;
        String Passenger8;
        final String Passenger9;
        String Passenger10;*/
        Passenger1=Pss1.getText().toString().trim();
        Passenger2=Pss2.getText().toString().trim();
        Passenger3=Pss3.getText().toString().trim();
        Passenger4=Pss4.getText().toString().trim();
        Passenger5=Pss5.getText().toString().trim();

        try
        {
            if(TextUtils.isEmpty(Pss6.getText().toString()))
            {
                Passenger6="NA";
                Passenger7="NA";
                Passenger8="NA";
                Passenger9="NA";
                Passenger10="NA";

            }
            else if(TextUtils.isEmpty(Pss7.getText().toString()))
            {
                Passenger6=Pss6.getText().toString().trim();
                Passenger7="NA";//Pss7.getText().toString().trim();
                Passenger8="NA";
                Passenger9="NA";
                Passenger10="NA";

            }
            else if(TextUtils.isEmpty(Pss8.getText().toString()))
            {
                Passenger6=Pss6.getText().toString().trim();
                Passenger7=Pss7.getText().toString().trim();
                Passenger8="NA";//Pss8.getText().toString().trim();
                Passenger9="NA";
                Passenger10="NA";
            }
            else if(TextUtils.isEmpty(Pss9.getText().toString()))
            {

                Passenger6=Pss6.getText().toString().trim();
                Passenger7=Pss7.getText().toString().trim();
                Passenger8=Pss8.getText().toString().trim();
                Passenger9="NA";
                Passenger10="NA";
            }
            else if(TextUtils.isEmpty(Pss10.getText().toString()))
            {

                Passenger6=Pss6.getText().toString().trim();
                Passenger7=Pss7.getText().toString().trim();
                Passenger8=Pss8.getText().toString().trim();
                Passenger9=Pss9.getText().toString().trim();
                Passenger10="NA";
            }
            else {
                Passenger6=Pss6.getText().toString().trim();
                Passenger7=Pss7.getText().toString().trim();
                Passenger8=Pss8.getText().toString().trim();
                Passenger9=Pss9.getText().toString().trim();//Passenger6=Pss6.getText().toString().trim();
                Passenger10=Pss10.getText().toString().trim();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Make sure no empty field precedes a non-empty field",Toast.LENGTH_LONG).show();
        }


        databaseReferenceVerified.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if(dataSnapshot.hasChild(Passenger1)&&dataSnapshot.hasChild(Passenger2)&&dataSnapshot.hasChild(Passenger3)&&dataSnapshot.hasChild(Passenger4)&&dataSnapshot.hasChild(Passenger5)&&dataSnapshot.hasChild(Passenger6)&&dataSnapshot.hasChild(Passenger7)&&dataSnapshot.hasChild(Passenger8)&&dataSnapshot.hasChild(Passenger9)&&dataSnapshot.hasChild(Passenger10))
                {
                    FinalizeForm();
                    NotifyAllPassengers();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Make Sure all passes are verified",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // final ProgressDialog Upload =new ProgressDialog(getApplicationContext());





    }

    private void FinalizeForm() {
        VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("DriverLicense").putFile(DRIVER_LICENSE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotDRIVERLICENSE) {


                VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("Documents").putFile(DOCUMENTS_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshotDOCUMENTS) {

                        VEHICLE_STORAGE_REF.child(PASS_WHICH_BOOKED).child("Insurance").putFile(INSURANCE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshotINSURANCE) {



                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("Documents_LINK").setValue(taskSnapshotDOCUMENTS.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("DriverLicense_LINK").setValue(taskSnapshotDRIVERLICENSE.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("Insurance_LINK").setValue(taskSnapshotINSURANCE.getDownloadUrl().toString());
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("DriverName").setValue(DRIVER_NAME_STRING);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerOne").setValue(Passenger1);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTwo").setValue(Passenger2);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerThree").setValue(Passenger3);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerFour").setValue(Passenger4);
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerFive").setValue(Passenger5);

                                if(TextUtils.isEmpty(Pss6.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSix").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSix").setValue(Pss6.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss7.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue(Pss7.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss8.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerEight").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerEight").setValue(Pss8.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss9.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerNine").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerNine").setValue(Pss9.getText().toString());
                                }
                                if(TextUtils.isEmpty(Pss10.getText().toString()))
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTen").setValue("N/A");
                                }
                                else
                                {
                                    VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTen").setValue(Pss10.getText().toString());
                                }
                                VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("OperatorName").setValue(OPERATOR_NAME_STRING).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent notify = new Intent();

                                        notify.putExtra("Values", "Vehicle booked for pass_no "+PASS_WHICH_BOOKED);

                                        notify.setAction("Pas_with_some_value_has_changed");

                                        sendBroadcast(notify);



                                        //  Toast.makeText(getApplicationContext(),"Uploading Successful",Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                        Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();


                                    }
                                });




                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"Uploading Failed...",Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void NotifyAllPassengers() {
        if(!TextUtils.isEmpty(Pss1.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss1.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss2.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss2.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss3.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss3.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss4.getText().toString()))
        {

            final String ApplicationNo;
            ApplicationNo=Pss4.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss5.getText().toString()))
        {
            // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss5.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }

      // String UID;

        if(!TextUtils.isEmpty(Pss6.getText().toString()))
        {
           // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSix").setValue("N/A");

                final String ApplicationNo;
                ApplicationNo=Pss6.getText().toString().trim();
                APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String UID;
                        UID= dataSnapshot.getValue(String.class);

                        APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("Applied for vehicle by user with pass"+ApplicationNo);
                        APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });
        }
        if(!TextUtils.isEmpty(Pss7.getText().toString()))
        {
           // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerSeven").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss7.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss8.getText().toString()))
        {
           // VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerEight").setValue("N/A");




            final String ApplicationNo;
            ApplicationNo=Pss8.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss9.getText().toString()))
        {

            final String ApplicationNo;
            ApplicationNo=Pss9.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
        if(!TextUtils.isEmpty(Pss10.getText().toString()))
        {
          //  VEHICLE_DATABASE_REF.child(PASS_WHICH_BOOKED).child("PassengerTen").setValue("N/A");

            final String ApplicationNo;
            ApplicationNo=Pss10.getText().toString().trim();
            APPLICATION_REF.child("Applications").child(ApplicationNo).child("Uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String UID;
                    UID= dataSnapshot.getValue(String.class);

                    APPLICATION_REF.child("Users").child(UID).child("Applications").child(ApplicationNo).setValue("A vehicle has been booked for apllication"+ApplicationNo);
                    APPLICATION_REF.child("Applications").child(ApplicationNo).child("ApplicationStatus").setValue("Applied for vehicle by user with pass"+ApplicationNo);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==DRIVER_LICENSE_REQUEST_CODE)
        {
            //When Applicant photo is selected

                    DRIVER_LICENSE_URI=data.getData();

                    DriverLicense.setImageURI(DRIVER_LICENSE_URI);



        }
        else if(resultCode==RESULT_OK && requestCode==DOCUMENTS_REQUEST_CODE)
        {
            //when applicant scanned user id is selected

                DOCUMENTS_URI=data.getData();

                Documents.setImageURI(DOCUMENTS_URI);


        }
        else if(resultCode==RESULT_OK && requestCode==INSURANCE_REQUEST_CODE)
        {
            //when applicant scanned user id is selected

            INSURANCE_URI=data.getData();

            Insurance.setImageURI(INSURANCE_URI);


        }

    }

}
