package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView raidHora;
    private TextView raidHoraDefault;
    private TextView pokemonRaidDefault;
    private TextView pokemonRaid;
    private ImageView gymImage;
    private ImageView raidPokemonImage;
    private Button incursionButton;
    private Button crearIncursion;
    private Button raidParticipantesButton;
    private Button raidChat;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private LinearLayout linearLayout2;


    //import from intent
    Bundle bundle;

    Gym gym= new Gym();
    Raid raid=new Raid();


    //vars
    int totalPlayers1;
    int totalPlayers2;
    int totalPlayers3;
    String raidParticipante;

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
        raidHora=findViewById(R.id.raidHora);
        raidHoraDefault=findViewById(R.id.raidHoraDefault);
        raidPokemonImage=findViewById(R.id.raidPokemonImage);
        linearLayout2=findViewById(R.id.linearLayout2);
        raidParticipantesButton=findViewById(R.id.raidParticipantesButton);
        pokemonRaidDefault=findViewById(R.id.pokemonRaidDefault);
        pokemonRaid=findViewById(R.id.pokemonRaid);
        raidChat=findViewById(R.id.chatRaid);


        crearIncursion.setOnClickListener(this);
        incursionButton.setOnClickListener(this);
        raidParticipantesButton.setOnClickListener(this);
        raidChat.setOnClickListener(this);

        incursionButton.setVisibility(View.INVISIBLE);
        raidPokemonImage.setVisibility(View.INVISIBLE);
        raidHoraDefault.setVisibility(View.INVISIBLE);
        raidHora.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        pokemonRaidDefault.setVisibility(View.INVISIBLE);
        pokemonRaid.setVisibility(View.INVISIBLE);
        raidParticipantesButton.setVisibility(View.INVISIBLE);
        raidChat.setVisibility(View.INVISIBLE);
        raidParticipantesButton.setVisibility(View.INVISIBLE);

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
                gym=snapshot.getValue(Gym.class);
                raid=gym.getRaid();
                if (snapshot.hasChild("raid")) {
                    final int id = getResources().getIdentifier("com.example.jordi.raidfinder:drawable/" + raid.getPokemon().toLowerCase(), null, null);
                    linearLayout2.setVisibility(View.VISIBLE);
                    incursionButton.setVisibility(View.VISIBLE);
                    crearIncursion.setVisibility(View.INVISIBLE);
                    raidHora.setVisibility(View.VISIBLE);
                    raidHoraDefault.setVisibility(View.VISIBLE);
                    raidHora.setText(String.valueOf(raid.getHora()));
                    raidPokemonImage.setVisibility(View.VISIBLE);
                    raidPokemonImage.setImageResource(id);
                    pokemonRaidDefault.setVisibility(View.VISIBLE);
                    pokemonRaid.setVisibility(View.VISIBLE);
                    pokemonRaid.setText(raid.getPokemon());
                    filterPlayersTeam();

                    if (raid.getParticipantes().contains(mAuth.getCurrentUser().getUid())){
                        incursionButton.setVisibility(View.INVISIBLE);
                        raidChat.setVisibility(View.VISIBLE);
                        raidParticipantesButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void filterPlayersTeam() {
        raid=gym.getRaid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase.child("gym").child(gym.getGym_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gym=dataSnapshot.getValue(Gym.class);
                totalPlayers1=0;
                totalPlayers2=0;
                totalPlayers3=0;
                for (int i = 0; i <raid.getParticipantes().size(); i++) {
                    raidParticipante = raid.getParticipantes().get(i);
                    mDatabase.child("users").child(raidParticipante).child("equipo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (Integer.parseInt(String.valueOf(dataSnapshot.getValue()))==1){
                                totalPlayers1++;
                            }
                            else if (Integer.parseInt(String.valueOf(dataSnapshot.getValue()))==2) {
                                totalPlayers2++;
                            }
                            else if (Integer.parseInt(String.valueOf(dataSnapshot.getValue()))==3) {
                                totalPlayers3++;
                            }
                            totalPlayersTeam1.setText(String.valueOf(totalPlayers1));
                            totalPlayersTeam2.setText(String.valueOf(totalPlayers2));
                            totalPlayersTeam3.setText(String.valueOf(totalPlayers3));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });//Esto debería refrescar el gimnasio al entrar alguien en la raid
    }

    @Override
    public void onClick(View view) {
        raid=gym.getRaid();
        if (view.equals(crearIncursion)){
            Intent intent=new Intent(this,raidDataActivity.class);
            startActivityForResult(intent,CODE_RAID);
            Toast.makeText(this, "gimnasio con la id "+gym.getGym_id(), Toast.LENGTH_LONG).show();
        }
        if (view.equals(incursionButton)){
            joinRaid();
        }
        if (view.equals(raidParticipantesButton)){
            Intent intent=new Intent(this,ParticipantesIncursionActivity.class);
            intent.putExtra("raid",raid.objectToJson());
            startActivity(intent);
        }
        if(view.equals(raidChat)){
            Intent intent=new Intent(this,ChatActivity.class);
            intent.putExtra("gymid",gym.getGym_id());
            startActivity(intent);
        }

    }
    public void joinRaid(){
        raid=gym.getRaid();
        raid.getParticipantes().add(mAuth.getCurrentUser().getUid());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("gym").child(gym.getGym_id());

        //check if user already joined the raid
        if (raid.getParticipantes().contains(mAuth.getCurrentUser().getUid())){
            Toast.makeText(this, "Ya estás inscrito", Toast.LENGTH_LONG).show();
            incursionButton.setVisibility(View.INVISIBLE);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            raid=gym.getRaid();

            gym.setRaid(raid);
            mDatabase.child("gym").child(gym.getGym_id()).setValue(gym);
        }
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
        setGymData();
    }
}
