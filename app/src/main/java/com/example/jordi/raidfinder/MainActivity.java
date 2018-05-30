package com.example.jordi.raidfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button loginBoton;
    private TextView registerMainText;
    private ImageView logo;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private DatabaseReference mDatabase;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        registerMainText = findViewById(R.id.registerMainText);
        loginBoton = findViewById(R.id.loginBoton);
        loginBoton.setOnClickListener(this);
        logo = findViewById(R.id.logo);
        saveLoginCheckBox = findViewById(R.id.checkBox);

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();



        if (saveLogin == true) {
            emailLogin.setText(loginPreferences.getString("username", ""));
            passwordLogin.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    public void registrarUsuario(View view) throws JSONException {

        Intent intent = new Intent(this, userRegister.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(loginBoton)) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailLogin.getWindowToken(), 0);

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", emailLogin.getText().toString());
                loginPrefsEditor.putString("password",  passwordLogin.getText().toString());
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }



            final String email = String.valueOf(emailLogin.getText());
            final String password = String.valueOf(passwordLogin.getText());
            if (email.isEmpty() || password.isEmpty()) {
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
                                    Intent intent = new Intent(getApplicationContext(), MainMapActivity.class);
                                    startActivity(intent);
                                    finish();
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
    }




