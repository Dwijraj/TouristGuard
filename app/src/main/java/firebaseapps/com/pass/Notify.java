package firebaseapps.com.pass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 1405214 on 04-10-2016.
 *
 * THis broadcast reciever recieves for a particular action and then fires notification
 * relevant to the event occured
 */
public class Notify extends BroadcastReceiver {
    NotificationCompat.Builder notification;            //Notification builder object
    private static final int UNIQUE_ID=123476776;
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras=intent.getExtras();
        String value=extras.getString("Values");

        notification= new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);


        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("Notification from Pass");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Pass");
        notification.setContentText(value);

        Intent intents=new  Intent(context,CheckPassDetails.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intents,PendingIntent.FLAG_NO_CREATE);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm=(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(UNIQUE_ID,notification.build());


    }
}
