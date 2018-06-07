package com.raid.jordi.raidfinder;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView userProfileTeam;
    private EditText userProfileName;
    private EditText userProfileLevel;
    private TextView cambiarPassword;
    private TextView cambiarEmail;
    private TextView verAnuncio;
    private Button button2;
    private Button button3;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    User user;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userProfileTeam=findViewById(R.id.userProfileTeam);
        userProfileName=findViewById(R.id.userProfileName);
        userProfileLevel=findViewById(R.id.userProfileLevel);
        cambiarPassword=findViewById(R.id.cambiarPassword);
        cambiarEmail=findViewById(R.id.cambiarEmail);
        //verAnuncio=findViewById(R.id.verAnuncio);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        mAuth = FirebaseAuth.getInstance();
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        cambiarEmail.setOnClickListener(this);
        cambiarPassword.setOnClickListener(this);
        setUserProfile();
        cambiarEmail.setVisibility(View.INVISIBLE);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4137175740990129/4923852996");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
    private void setUserProfile(){
        user=new User();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                userProfileName.setText(user.getNombre());
                userProfileLevel.setText(String.valueOf(user.getNivel()));
                if (user.getEquipo()==1){
                    userProfileTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_valor));
                } else if (user.getEquipo()==2){
                    userProfileTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_mystic));
                } else if (user.getEquipo()==3){
                    userProfileTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_instinct));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.equals(button2)){
            if (userProfileName.getText().toString().matches("")){
                Toast.makeText(this, "El nombre no puede ir vacío!", Toast.LENGTH_SHORT).show();
            } else if (userProfileLevel.getText().toString().matches("")) {
                    Toast.makeText(this, "El nivel no puede ir vacío!", Toast.LENGTH_SHORT).show();
                } else {
                user.setNivel(Integer.parseInt(String.valueOf(userProfileLevel.getText())));
                user.setNombre(userProfileName.getText().toString());
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                Toast.makeText(this, "Perfil guardado", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        if (view.equals(button3)){
            finish();
        }
        if (view.equals(cambiarEmail)){

        }
        if (view.equals(cambiarPassword)){
            if(mAuth != null) {
                mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                Toast.makeText(this, "Email enviado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(" error ", " bad entry ");
            }
        }
        if (view.equals(verAnuncio)){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        }
    }
}
