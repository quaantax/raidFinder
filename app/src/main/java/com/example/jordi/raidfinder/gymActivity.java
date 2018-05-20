package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class gymActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gymNameText;
    private TextView gymDefaultTitle;
    private TextView totalPlayersTeam1;
    private TextView totalPlayersTeam2;
    private TextView totalPlayersTeam3;
    private ImageView gymImage;
    private ImageView ImageTeam1;
    private Button incursionButton;
    private Button crearIncursion;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RelativeLayout linearLayout2;


    //import from intent
    Bundle bundle;

    Gym gym;

    public static final int CODE_RAID=42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        mAuth = FirebaseAuth.getInstance();

        gymImage=findViewById(R.id.gymImage);
        gymNameText=findViewById(R.id.gymName);
        incursionButton=findViewById(R.id.incursionButton);
        totalPlayersTeam1=findViewById(R.id.totalPlayersTeam1);
        totalPlayersTeam2=findViewById(R.id.totalPlayersTeam2);
        totalPlayersTeam3=findViewById(R.id.totalPlayersTeam3);
        crearIncursion=findViewById(R.id.crearIncursion);


        crearIncursion.setOnClickListener(this);
        incursionButton.setOnClickListener(this);


        Intent intent=getIntent();
        bundle =intent.getExtras();

        setGymData();
    }
    public void setGymData(){
        gym = new Gym();
        gym=gym.JsonToObject(bundle.getString("gym"));

        Picasso.get().load(gym.getUrl()).into(gymImage);
        gymNameText.setText(gym.getName());

        incursionButton.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onClick(View view) {

            if (view.equals(crearIncursion)){
                //crearIncursion();

                incursionButton.setVisibility(View.VISIBLE);
                Intent intent=new Intent(this,raidDataActivity.class);
                startActivityForResult(intent,CODE_RAID);

                Toast.makeText(this, "gimnasio con la id "+gym.getGym_id(), Toast.LENGTH_LONG).show();
        }
        if (view.equals(incursionButton)){
                joinRaid();
        }

    }
    public void joinRaid(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_RAID && resultCode==RESULT_OK){
            Raid raid=new Raid();
            raid=raid.JsonToObject(data.getStringExtra("Incursion"));
            gym.setRaid(raid);
            crearIncursion();


        }
    }

    public void crearIncursion(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("gym").child(gym.getGym_id()).setValue(gym);
        Log.d("gymAct",gym.getGym_id());
    }
}
