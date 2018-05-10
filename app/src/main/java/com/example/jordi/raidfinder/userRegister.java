package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userRegister extends AppCompatActivity {
    private EditText correoRegistro;
    private EditText passwordRegistro;
    private EditText nombreRegistro;
    private EditText nivelRegistro;
    private Button botonRegistro;
    private FirebaseAuth mAuth;


    private DatabaseReference mDatabase;

    private RadioGroup group;
// ...


    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        mAuth = FirebaseAuth.getInstance();


        correoRegistro= findViewById(R.id.correoRegistro);
        passwordRegistro= findViewById(R.id.passwordRegistro);
        group = findViewById(R.id.radioGroup);
        botonRegistro= findViewById(R.id.botonRegistro);
        nombreRegistro=findViewById(R.id.nombreRegistro);
        nivelRegistro=findViewById(R.id.nivelRegistro);
    }

   

    public void registroUsuario(View view) {

        mAuth.createUserWithEmailAndPassword(String.valueOf(correoRegistro.getText()), String.valueOf(passwordRegistro.getText()))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            writeNewUser(user.getUid(),user);
                            Intent intent = new Intent(getApplicationContext(), MainMapActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void writeNewUser(String userId, FirebaseUser user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = group.indexOfChild(radioButton);

        User newUser= new User(user.getEmail(),nombreRegistro.getText().toString(),idx,Integer.parseInt(nivelRegistro.getText().toString()));

        mDatabase.child("users").child(userId).setValue(newUser);


    }


}
