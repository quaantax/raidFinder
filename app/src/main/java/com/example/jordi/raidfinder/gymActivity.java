package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class gymActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gymNameText;
    private TextView gymDefaultTitle;
    private TextView totalPlayersTeam1;
    private ImageView gymImage;
    private ImageView ImageTeam1;
    private Button incursionButton;
    private Button crearIncursion;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    //import from intent
    Bundle bundle;

    String gymNameData;
    String gymUrl;
    String gymId;

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
        gymDefaultTitle=findViewById(R.id.gymDefaultTitle);
        totalPlayersTeam1=findViewById(R.id.totalPlayersTeam1);
        ImageTeam1=findViewById(R.id.ImageTeam1);
        crearIncursion=findViewById(R.id.crearIncursion);

        crearIncursion.setOnClickListener(this);

        Intent intent=getIntent();
        bundle =intent.getExtras();

        setGymData();
    }
    public void setGymData(){
 /*     gymNameData=(String) bundle.get("gymName");
        gymUrl=(String) bundle.get("gymUrl");
        gymId=(String) bundle.get("gymId");

        gym=new Gym();
        gym.setName(gymNameData);
        gym.setUrl(gymUrl);
        gym.setGym_id(gymId);*/
        gym=new Gym();
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

        }

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

        mDatabase.child("gym").child("742").setValue(gym);
        Log.d("gymAct",gym.getGym_id());
    }
}
