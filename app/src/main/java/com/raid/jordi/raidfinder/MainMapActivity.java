package com.raid.jordi.raidfinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private String TAG;
    private List<Gym> listGym;
    private String userId;
    private User user;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView userName;
    private TextView userNivel;
    private ImageView userTeam;
    private ImageView botonProfile;

    private String gymId;
    Location currentLocation;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int DEFAULT_ZOOM = 16;


    //vars
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        checkGpsAvaiable();
        getLocationPermission();
        //se inicializa la instancia de Firebase
        mAuth = FirebaseAuth.getInstance();
        //initMap();
        listGym = new ArrayList<>();
        userName = findViewById(R.id.userName);
        userNivel = findViewById(R.id.userNivel);
        userTeam = findViewById(R.id.userTeam);
        botonProfile=findViewById(R.id.botonProfile);

        botonProfile.setOnClickListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            getFirebaseUser();
            boolean emailVerified = user.isEmailVerified();
        }
    }
    private void getFirebaseUser(){
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                user=dataSnapshot.getValue(User.class);
                userName.setText(user.getNombre());
                userNivel.setText(String.valueOf(user.getNivel()));
                if (user.getEquipo()==1){
                    userTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_valor));
                } else if (user.getEquipo()==2){
                    userTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_mystic));
                } else {
                    userTeam.setImageDrawable(getResources().getDrawable(R.drawable.team_instinct));
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        ref.addValueEventListener(userListener);
    }

    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found location");
                            currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "Location not found");
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            //getLocationPermission();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }
        final Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon);
        final Bitmap bMapRaid = BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon_raid);


        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("gym");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                //Get firebase info for each gym
                Gym gym=dataSnapshot.getValue(Gym.class);
                gym.setGym_id(dataSnapshot.getKey());


               //Set custom marker with firebase info
                LatLng newLocation= new LatLng(Double.parseDouble(gym.getLatitude()),Double.parseDouble(gym.getLongitude()));
                if (dataSnapshot.hasChild("raid")){
                    Marker marker= mMap.addMarker(new MarkerOptions()
                            .position(newLocation)
                            .title(gym.getName())
                            .snippet(gym.getUrl())
                            .icon(BitmapDescriptorFactory.fromBitmap(bMapRaid)));
                    marker.setTag(gym);
                } else {
                    Marker marker= mMap.addMarker(new MarkerOptions()
                            .position(newLocation)
                            .title(gym.getName())
                            .snippet(gym.getUrl())
                            .icon(BitmapDescriptorFactory.fromBitmap(bMap)));
                    marker.setTag(gym);
                }
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
               Gym gym=dataSnapshot.getValue(Gym.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                }


        };
        ref.addChildEventListener(childEventListener);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String gymName=String.valueOf(marker.getTag());
                String gymUrl=marker.getSnippet();

                Intent intent= new Intent(getApplicationContext(),gymActivity.class);
                Gym gym=(Gym)marker.getTag();
                intent.putExtra("gym",gym.objectToJson());
                intent.putExtra("gymName",gymName);
                intent.putExtra("gymUrl",gymUrl);
                intent.putExtra("gymId",gymId);
                startActivity(intent);


                return true;
            }
        });
    }//onMapReady

    private void getLocationPermission(){
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext()
                ,FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext()
                    ,COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    //initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i=0; i<grantResults.length; i++){
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted=true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    private void initMap(){
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainMapActivity.this);

    }

    public void openGymActivity(){
        Intent intent =new Intent(this,gymActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        //apretar 2 veces el botón de atrás para salir
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona atrás otra vez para salir", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void checkGpsAvaiable(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("tu GPS está desactivado, ¿quieres activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            Toast.makeText(MainMapActivity.this, "Esta app no funciona sin GPS", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (isGooglePlayServicesAvailable(this)){
                initMap();
            } else {
                Toast.makeText(this, "Tu versión de Google Play Services es incompatible o no funciona", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        if (view.equals(botonProfile)){
            Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
            startActivity(intent);
        }
    }
}