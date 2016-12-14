package firebaseapps.com.pass;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class Startup extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;


        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_startup);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Startup.this,ApplyPass.class);
                    Startup.this.startActivity(mainIntent);
                    Startup.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


}
