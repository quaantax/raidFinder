package com.raid.jordi.raidfinder;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.info_window, null);


        TextView title = view.findViewById(R.id.title);
        ImageView image = view.findViewById(R.id.gymImage);
        Button button=view.findViewById(R.id.button);

        title.setText(marker.getTitle());
        //Picasso.get().load(marker.getSnippet()).into(image);
        image.setImageResource(R.drawable.gym);



        return view;
    }




}
