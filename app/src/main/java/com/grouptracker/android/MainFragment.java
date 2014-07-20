package com.grouptracker.android;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tieru on 6/29/14.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    Button trackButton;
    Button clearData;
    ListView listView;
    Context context;
    ApplicationState applicationState;
    LocationDatabase database;
    int currentTrackID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        trackButton = (Button) view.findViewById(R.id.track_button);
        trackButton.setOnClickListener(this);
        clearData = (Button) view.findViewById(R.id.clear_data);
        clearData.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.location_list);
        context = getActivity();
        database=new LocationDatabase(context);
        database.open();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMyServiceRunning(TrackService.class)){
            trackButton.setText(R.string.stop_tracking);
        }
        else {
            trackButton.setText(R.string.start_tracking);
        }
        currentTrackID = database.getLastTrackID();
        fillListView();
        applicationState = ApplicationState.getInstance();
    }

    private void startService(){
        context.startService(new Intent(context, TrackService.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.track_button:
                if (isMyServiceRunning(TrackService.class)){
                    trackButton.setText(R.string.start_tracking);
                    context.stopService(new Intent(context, TrackService.class));
                }
                else {
                    trackButton.setText(R.string.stop_tracking);
                    startService();
                }
                break;
            case R.id.clear_data:
                database.deleteAllLocations();
                fillListView();
                break;
        }
    }
    private void fillListView(){
        Cursor cursor = database.getLocations();
        SimpleCursorAdapter dataAdapter;
        Log.d("MYAPP", "Lines returned: " + cursor.getCount());
        dataAdapter = new SimpleCursorAdapter(context, R.layout.location_list_item, cursor,
                new String[]{"_id",LocationDatabase.LOCATION_TABLE_COLUMN2,LocationDatabase.LOCATION_TABLE_COLUMN3},
                new int[]{R.id.id, R.id.latitude, R.id.longitude}, 0);

        listView.setAdapter(dataAdapter);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
