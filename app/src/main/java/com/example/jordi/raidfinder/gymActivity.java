package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class gymActivity extends AppCompatActivity {

    private TextView gymNameText;
    private ImageView gymImage;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        gymImage=findViewById(R.id.gymImage);
        gymNameText=findViewById(R.id.gymName);

        Intent intent=getIntent();
        bundle =intent.getExtras();

        setGymData();
    }
    public void setGymData(){
        String gymNameData=(String) bundle.get("gymName");
        String gymUrl=(String) bundle.get("gymUrl");

        Picasso.get().load(gymUrl).into(gymImage);
        gymNameText.setText(gymNameData);

    }

}
