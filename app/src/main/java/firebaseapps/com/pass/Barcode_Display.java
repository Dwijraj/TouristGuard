package firebaseapps.com.pass;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Barcode_Display extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_image);

        Intent i=getIntent();
        Bundle extras=i.getExtras();



        ImageView imageView=(ImageView) findViewById(R.id.BAR_CODE_SHOW);
        TextView  Pass=(TextView)findViewById(R.id.PassNumber);

        Glide.with(Barcode_Display.this)
                .load(extras.getString("Barcode_URL"))
                .into(imageView);

        Pass.setText(extras.getString("Pass_No"));

    }
}
