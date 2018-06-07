package com.raid.jordi.raidfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private EditText input;
    private FirebaseListAdapter<ChatMessage> adapter;

    Bundle bundle;
    String gymId;


    String nombreUsuario;
    User user=new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);

        getFirebaseUser();


        bundle = getIntent().getExtras();
        gymId=bundle.getString("gymid");
        //fab.setOnClickListener(this);

        Toast.makeText(this, ""+gymId, Toast.LENGTH_SHORT).show();
        displayChatMessages();

    }


  private void displayChatMessages(){
      ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

      adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
              R.layout.message, FirebaseDatabase.getInstance().getReference().child("gym").child(gymId).child("raid").child("chat")) {
          @Override
          protected void populateView(View v, ChatMessage model, int position) {
              // Get references to the views of message.xml
              TextView messageText = v.findViewById(R.id.message_text);
              TextView messageUser = v.findViewById(R.id.message_user);
              TextView messageTime = v.findViewById(R.id.message_time);


              mDatabase.child("users").child(model.getMessageUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      user=dataSnapshot.getValue(User.class);
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });





              messageText.setText(model.getMessageText());
              messageUser.setText(user.getNombre());

              // Format the date before showing it
              messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                      model.getMessageTime()));
          }
      };

      listOfMessages.setAdapter(adapter);
  }

    public void writeMessage(View view){
        EditText input = findViewById(R.id.input);
            FirebaseDatabase.getInstance()
                    .getReference().child("gym").child(gymId).child("raid").child("chat")
                    .push()
                    .setValue(new ChatMessage(input.getText().toString(),
                            mAuth.getCurrentUser().getUid())
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
