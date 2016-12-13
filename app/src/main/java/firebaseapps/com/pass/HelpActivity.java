package firebaseapps.com.pass;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class HelpActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGT = 16000;
    private ImageView DisplayView;
    AnimationDrawable frameAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        DisplayView=(ImageView)findViewById(R.id.DISPLAY_VIEW);




        DisplayView.setBackgroundResource(R.drawable.animaltion_list);

        frameAnimation = (AnimationDrawable) DisplayView.getBackground();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                finish();
            }
        }, SPLASH_DISPLAY_LENGT);


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Starting the animation when in Focus
            frameAnimation.start();
        } else {
            // Stoping the animation when not in Focus
            frameAnimation.stop();
        }
    }
}
