package com.raid.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class raidDataActivity extends AppCompatActivity {

    Spinner spinner;
    private EditText timeRaidHours;
    private EditText timeRaidMinutes;
    private Button aceptarRaidButton;
    private Button cancelarRaidButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raid_data);

        timeRaidHours=findViewById(R.id.timeRaidHours);
        timeRaidMinutes=findViewById(R.id.timeRaidMinutes);
        spinner=findViewById(R.id.spinner);
        aceptarRaidButton=findViewById(R.id.aceptarRaidButton);
        cancelarRaidButton=findViewById(R.id.cancelarRaidButton);
        mAuth = FirebaseAuth.getInstance();
        aceptarRaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearIncursion();
            }
        });

        /*AdView mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, "ca-app-pub-4137175740990129~1676788849");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        loadSpinner();
    }
    public void loadSpinner(){
        ArrayList<String> pokemon= new ArrayList<>();
        pokemon.add("Latios");
        pokemon.add("Latias");
        pokemon.add("HoOh");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pokemon);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
    public void crearIncursion(){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if (timeRaidMinutes.getText().toString().matches("") || timeRaidHours.getText().toString().matches("")){
                Toast.makeText(this, "La hora introducida no es correcta", Toast.LENGTH_SHORT).show();
            } else {
                if (Integer.parseInt(timeRaidHours.getText().toString())>=0 & Integer.parseInt(timeRaidHours.getText().toString())<24){
                    if (Integer.parseInt(timeRaidMinutes.getText().toString()) >=0 & Integer.parseInt(timeRaidMinutes.getText().toString()) <60){

                        Intent intent=new Intent(this,ParticipantesIncursionActivity.class);
                        Raid raid= new Raid();
                        raid.setHora(timeRaidHours.getText().toString()+":"+timeRaidMinutes.getText());
                        raid.setPokemon(spinner.getSelectedItem().toString());
                        raid.getParticipantes().add(mAuth.getCurrentUser().getUid());
                        intent.putExtra("Incursion",raid.objectToJson());
                        setResult(RESULT_OK,intent);
                        finish();


                    } else {
                        Toast.makeText(this, "La hora introducida es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "La hora introducida es incorrecta", Toast.LENGTH_SHORT).show();
                }
            }



        }
}
