package com.example.jordi.raidfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
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
            haveNetworkConnection();
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
                                    checkIfEmailVerified();

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
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Intent intent = new Intent(getApplicationContext(), MainMapActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Login correcto", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(this, "Correo no confirmado", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            //restart this activity
        }
    }
    private void haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        //return haveConnectedWifi || haveConnectedMobile;
        if (haveConnectedMobile == false & haveConnectedWifi==false ){
            Toast.makeText(this, "No tienes conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }
    }