package com.example.jordi.raidfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ParticipantesIncursionActivity extends AppCompatActivity {

    Adapter adapter;
    Raid raid;
    Bundle bundle;
    List<User> participantes;
    RecyclerView recyclerView;
    final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes_incursion);
        raid=new Raid();
        bundle = getIntent().getExtras();
        raid=raid.JsonToObject(bundle.getString("raid"));

        recyclerView = findViewById(R.id.your_recycler_view);
        recyclerView.setLayoutManager(layoutManager);




        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final List<User> participantes=new ArrayList<User>();
        //participantes.removeAll(participantes);
        for (int i = 0; i <raid.getParticipantes().size(); i++){
            //User user= new User();
            String raidParticipante=raid.getParticipantes().get(i);
            mDatabase.child("users").child(raidParticipante).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    participantes.add(user);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }//for
        adapter = new Adapter(participantes,this);
        recyclerView.setAdapter(adapter);
    }//oncreate

}
