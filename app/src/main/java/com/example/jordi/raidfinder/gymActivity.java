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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    Gym gym= new Gym();
    Raid raid;

    //vars
    int totalPlayers1=0;
    int totalPlayers2=0;
    int totalPlayers3=0;

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
        incursionButton.setVisibility(View.INVISIBLE);


        Intent intent=getIntent();
        bundle =intent.getExtras();

        setGymData();

    }
    public void setGymData(){
        gym=gym.JsonToObject(bundle.getString("gym"));

        Picasso.get().load(gym.getUrl()).into(gymImage);
        gymNameText.setText(gym.getName());


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("gym").child(gym.getGym_id());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("raid")) {
                    incursionButton.setVisibility(View.VISIBLE);
                    crearIncursion.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void filterPlayersTeam() {
        String raidParticipante = "";
        raid=gym.getRaid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        for (int i = 0; i <= raid.getParticipantes().size(); i++) {
            raidParticipante = raid.getParticipantes().get(i);
            DatabaseReference ref = database.getReference().child("users").child(raidParticipante).child("equipo");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    if (user.getEquipo()==1){
                        totalPlayers1++;
                    } else if (user.getEquipo()==2){
                        totalPlayers2++;
                    } else if (user.getEquipo()==3){
                        totalPlayers3++;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message

                }
            };
            ref.addValueEventListener(postListener);
            }


            totalPlayersTeam1.setText(String.valueOf(totalPlayers1));
            totalPlayersTeam2.setText(String.valueOf(totalPlayers2));
            totalPlayersTeam3.setText(String.valueOf(totalPlayers3));

    }


    @Override
    public void onClick(View view) {

            if (view.equals(crearIncursion)){
                //crearIncursion();


                Intent intent=new Intent(this,raidDataActivity.class);
                startActivityForResult(intent,CODE_RAID);

                Toast.makeText(this, "gimnasio con la id "+gym.getGym_id(), Toast.LENGTH_LONG).show();
        }
        if (view.equals(incursionButton)){
                //joinRaid();
                filterPlayersTeam();
        }

    }
    public void joinRaid(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("gym").child(gym.getGym_id());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        raid=gym.getRaid();
        raid.getParticipantes().add(mAuth.getCurrentUser().getUid());
        gym.setRaid(raid);
        mDatabase.child("gym").child(gym.getGym_id()).setValue(gym);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        ref.addValueEventListener(postListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_RAID && resultCode==RESULT_OK){
            raid=new Raid();
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
