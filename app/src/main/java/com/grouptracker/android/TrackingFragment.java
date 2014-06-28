package com.grouptracker.android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by tieru on 6/28/14.
 */


public class TrackingFragment extends Fragment{

    GoogleMap map;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMap();
    }

    private void initializeMap(){
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        map = mapFragment.getMap();
        if (map == null) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.unable_to_create_map, Toast.LENGTH_SHORT).show();
        }
    }
}