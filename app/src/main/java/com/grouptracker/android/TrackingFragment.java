package com.grouptracker.android;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by tieru on 6/28/14.
 */


public class TrackingFragment extends Fragment implements View.OnClickListener{

    GoogleMap map;
    Context context;
    LocationDatabase database;
    Button drawRouteButton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = getActivity();

        drawRouteButton = (Button) view.findViewById(R.id.draw_route_btn);
        drawRouteButton.setOnClickListener(this);

        database=new LocationDatabase(context);
        database.open();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMap();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void initializeMap(){
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        map = mapFragment.getMap();
        if (map == null) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.unable_to_create_map, Toast.LENGTH_SHORT).show();
        }
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.draw_route_btn:
                drawRoute();
                break;
        }
    }

    private void drawRoute() {
        PolylineOptions polylineOptions = new PolylineOptions();

        Cursor cursor = database.getLocations();
        int latitudeColIndex = cursor.getColumnIndex("latitude");
        int longitudeColIndex = cursor.getColumnIndex("longitude");

        if (!cursor.moveToFirst())
            return;

        do{
            polylineOptions.add(new LatLng(cursor.getFloat(latitudeColIndex), cursor.getFloat(longitudeColIndex)));
        } while (cursor.moveToNext());

        polylineOptions.color(Color.RED).width(10);

        Log.d("MYAPP", "Displaying route. " + polylineOptions.getPoints().size() + " dots");

        cursor.moveToLast();
        map.addPolyline(polylineOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cursor.getFloat(latitudeColIndex), cursor.getFloat(longitudeColIndex)), 16));

        cursor.close();
    }
}
