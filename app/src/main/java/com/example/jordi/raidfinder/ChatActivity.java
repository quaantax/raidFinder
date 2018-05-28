package com.example.jordi.raidfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private EditText input;
    Bundle bundle;
    String gymId;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);

        getFirebaseUser();


        bundle = getIntent().getExtras();
        gymId=bundle.getString("gymid");
        fab.setOnClickListener(this);

        Toast.makeText(this, ""+gymId, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View view) {
        if (view.equals(fab) ){
            writeMessage();
        }
    }


    private void writeMessage(){
        EditText input = findViewById(R.id.input);
            FirebaseDatabase.getInstance()
                    .getReference().child("gym").child(gymId).child("raid").child("chat")
                    .push()
                    .setValue(new ChatMessage(input.getText().toString(),
                            FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getDisplayName())
                    );

            input.setText("");

    }


    private void getFirebaseUser(){
        user=new User();
        mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user=dataSnapshot.getValue(User.class);
                    Toast.makeText(getApplicationContext(), ""+user.getNombre(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }


}
