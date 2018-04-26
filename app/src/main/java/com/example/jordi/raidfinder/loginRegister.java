package com.example.jordi.raidfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class    loginRegister extends AppCompatActivity {
    private EditText correoRegistro;
    private EditText passwordRegistro;
    private Button botonRegistro;
    private FirebaseAuth mAuth;

    private static final String TAG = "MyActivity";
    User userRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        correoRegistro=(EditText) findViewById(R.id.correoRegistro);
        passwordRegistro=(EditText) findViewById(R.id.passwordRegistro);
        botonRegistro=(Button) findViewById(R.id.botonRegistro);
        mAuth = FirebaseAuth.getInstance();

    }

   

    public void registroUsuario(View view) {

        userRegistro.setEmail(String.valueOf(correoRegistro.getText()));
        userRegistro.setPassword(String.valueOf(passwordRegistro.getText()));
        mAuth.createUserWithEmailAndPassword(userRegistro.getEmail(), userRegistro.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
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


}
