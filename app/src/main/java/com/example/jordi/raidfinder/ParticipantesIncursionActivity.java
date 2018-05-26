package com.example.jordi.raidfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


public class ParticipantesIncursionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Raid raid=new Raid();
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.your_recycler_view);
        setContentView(R.layout.activity_participantes_incursion);



        bundle = getIntent().getExtras();
        raid=raid.JsonToObject(bundle.getString("raid"));
        Toast.makeText(this, String.valueOf(raid.getParticipantes().get(1)), Toast.LENGTH_SHORT).show();
    }

}
