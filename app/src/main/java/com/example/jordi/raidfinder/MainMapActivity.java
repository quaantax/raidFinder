package com.example.jordi.raidfinder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

       ref.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   double location_left=dataSnapshot.child("latitude").getValue(Double.class);
                   double location_right=dataSnapshot.child("longitude").getValue(Double.class);
                   LatLng gym = new LatLng(location_left,location_right);
                   mMap.addMarker(new MarkerOptions().position(gym).title("Gym"));
                   //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gym,18));

               } else {
                   Log.e(TAG,"onDataChange: NoData");
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
           }
       });
    }
}
