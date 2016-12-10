package firebaseapps.com.pass;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by 1405214 on 25-09-2016.
 * This class is used to enable persistance and
 */

public class Pass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
