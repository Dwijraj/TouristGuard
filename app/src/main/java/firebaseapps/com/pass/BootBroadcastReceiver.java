package firebaseapps.com.pass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by 1405214 on 04-10-2016.
 * The broadcast receiver it listens  to broadcasts associated with
 * phone booting up and network changes
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)) {            //Executed when phone boots up
            //Service
            String status = NetworkUtil.getConnectivityStatusString(context);

            if(status.equals("Wifi enabled") || status.equals("Mobile data enabled"))
            {
             /*   Intent serviceIntent = new Intent(context, MyService.class);
                context.startService(serviceIntent);*/
            }

        }
        else {
            String status = NetworkUtil.getConnectivityStatusString(context);       //Gets the network status

            if (status.equals("Wifi enabled") || status.equals("Mobile data enabled")) {
                Intent serviceIntent = new Intent(context, MyService.class);        //Starts the service whenever connected
                context.startService(serviceIntent);
            }
            if (status.equals("Not connected to Internet")) {
                Intent serviceIntent = new Intent(context, MyService.class);
                context.stopService(serviceIntent);                                 //Stops the service whenever disconnected
            }
        }
    }
}
