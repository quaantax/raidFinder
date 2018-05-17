package com.example.jordi.raidfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button loginBoton;
    private TextView registerMainText;
    private ImageView logo;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        emailLogin=(EditText) findViewById(R.id.emailLogin);
        passwordLogin=(EditText) findViewById(R.id.passwordLogin);
        registerMainText=(TextView) findViewById(R.id.registerMainText);
        loginBoton=(Button) findViewById(R.id.loginBoton);
        loginBoton.setOnClickListener(this);
        logo=(ImageView) findViewById(R.id.logo);





    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    public void registrarUsuario(View view) throws JSONException {

        /*Intent intent = new Intent(this, userRegister.class);
        startActivity(intent);*/
        pushJson();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(loginBoton)) {

            final String email= String.valueOf(emailLogin.getText());
            final String password=String.valueOf(passwordLogin.getText());
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(MainActivity.this, "La autenticación ha fallado",
                        Toast.LENGTH_LONG).show();
            } else {


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Intent intent= new Intent(getApplicationContext(), MainMapActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "La autenticación ha fallado",
                                        Toast.LENGTH_LONG).show();
                                //updateUI(null);
                            }
                        }
                    });
            }//final del if que comprueba si el email o la contraseña estan vacios
        }
    }
    public String getGymJSON(){
        return null;
    }
    public void pushJson(){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Gym gym = new Gym();
            gym.setLatitude("39.466524");
            gym.setLongitude("-0.455845");
            gym.setName("Escultura A Paco El Perro Flaco");
            gym.setUrl("http://lh3.ggpht.com/EdEi5QU-YXxgN4D4KPEvAB7SAiVUM7bE2qOq0UoO8dU4gE7E8lZjt4AHuk95eXzXxtozjhjZeNf1xIZyYK9ekw");
            mDatabase.child("gym").push().setValue(gym);
        }
    }

