package firebaseapps.com.pass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**This service checks for change in application status of the logged in user if there is change in any of
 * his applied pass
 * this service sends a croadcast with a definite action which intern is responsible to firing
 * notifications
 */


public class MyService extends Service {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    public static int count;
    public MyService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {       //Runs for the very first time the service runs
        super.onStart(intent, startId);

        Toast.makeText(getApplicationContext(),"Checking application status",Toast.LENGTH_LONG).show();
        count=0;

    }

    @Override
    public void onDestroy() {                               //Runs when service is destroyed
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Can't check application status please check network connectivity",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  //Runs everytime the service starts



            mAuth = FirebaseAuth.getInstance();                 //Gets the firebase auth

     if(mAuth.getCurrentUser()!=null) {

         //It has the reference to the applications of particular user
         databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Applications");
         Log.v("Maina", "start4");
         databaseReference.keepSynced(true);

         databaseReference.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {


             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {

//                   Comment newComment = dataSnapshot.getValue(Comment.class);
                //Gets the key of what is changed
                 String commentKey = dataSnapshot.getKey();

                 //Reduces the length of pass number
                 char[] a = new char[20];
                 commentKey.getChars(0, 16, a, 0);


                 Intent notify = new Intent();
                 notify.putExtra("Values", "Pass " + String.valueOf(a) + "... status changed");
                 //  notify.putExtra("Value",newComment.getTextContent());
                 notify.setAction("Pas_with_some_value_has_changed");
                 Log.v("Maina", "start6");
                 sendBroadcast(notify);
                 Log.v("Maina", "start5");
                 Log.v("Maina", "start7");

                 databaseReference.keepSynced(true);

             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
     }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
