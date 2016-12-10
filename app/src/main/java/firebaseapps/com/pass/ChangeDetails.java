package firebaseapps.com.pass;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ChangeDetails extends AppCompatActivity {

    private EditText Passno;
    private EditText Idno;
    private EditText DOJ;
    private Button update;
    private DatabaseReference mDatabaseref;
    private String passno;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private Intent intent;
    private int N=1;

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

        Passno=(EditText)findViewById(R.id.change_pass_number);
        Idno=(EditText)findViewById(R.id.change_idno);
        DOJ=(EditText)findViewById(R.id.change_DOJ);
        update=(Button)findViewById(R.id.change_button);
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");

        intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);



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

                                    if(id_nos.equals(id_no))
                                    {
                                        if(N==1) {
                                            getPayments();
                                            N=99;
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Wrong Id no",Toast.LENGTH_LONG).show();
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
                        String state=response.getString("state");

                        Log.v("Here",state);

                        if(state.equals("approved"))
                        {
                           DatabaseReference changeDate=mDatabaseref.child(passno).child("DateOfJourney");      //Reference to Date of Journey
                            String DOJ_new=DOJ.getText().toString().trim();             //Gets the new DOJ

                            changeDate.setValue(DOJ_new).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {     //Sets the new value in Database
                                    Toast.makeText(getApplicationContext(),"Changes made successfully",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
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
