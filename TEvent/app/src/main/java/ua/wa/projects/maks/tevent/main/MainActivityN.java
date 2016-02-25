package ua.wa.projects.maks.tevent.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ua.wa.projects.maks.tevent.R;
import ua.wa.projects.maks.tevent.main.TEvent;

public class MainActivityN extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n);
        Button buttonNWS = (Button)findViewById(R.id.button);
        Button buttonCLD = (Button)findViewById(R.id.button2);
        ImageView imageView = (ImageView)findViewById(R.id.image1);

        imageView.setImageResource(R.drawable.webacademy);

        buttonNWS.setOnClickListener(this);
        buttonCLD.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Intent intentNWS = new Intent(this,TEvent.class);
                startActivity(intentNWS);
                break;
            case R.id.button2:
                Intent intentCLD = new Intent(this,ActivityCLD.class);
                startActivity(intentCLD);
                break;
        }

    }
}
