package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class raidDataActivity extends AppCompatActivity {

    Spinner spinner;
    private EditText timeRaid;
    private Button aceptarRaidButton;
    private Button cancelarRaidButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raid_data);

        timeRaid=findViewById(R.id.timeRaid);
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

        loadSpinner();
    }
    public void loadSpinner(){
        ArrayList<String> pokemon= new ArrayList<>();
        pokemon.add("Latios");
        pokemon.add("Latias");
        pokemon.add("Ho-Oh");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pokemon);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
    public void crearIncursion(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent=new Intent(this,ParticipantesIncursionActivity.class);
        Raid raid= new Raid();
        raid.setHora(timeRaid.getText().toString());
        raid.setPokemon(spinner.getSelectedItem().toString());
        raid.getParticipantes().add(mAuth.getCurrentUser().getUid());
        intent.putExtra("Incursion",raid.objectToJson());
        setResult(RESULT_OK,intent);
        finish();
    }


}
