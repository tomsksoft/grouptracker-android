package com.grouptracker.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tieru on 6/29/14.
 */
public class TrackService extends Service {

    Context context;
    LocationDatabase database;
    LocationManager locMgr;
    CustomLocationListener locLstnr;
    int currentTrackID;

    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        beginTracking();
        Log.d("SERVICE_DEBUG", "On start command");
        return START_REDELIVER_INTENT;
    }

    public void onDestroy() {
        Log.d("SERVICE_DEBUG", "On stop command");
        locMgr.removeUpdates(locLstnr);
        locMgr = null;
        database.close();
        super.onDestroy();
    }

    private void beginTracking(){
        database=new LocationDatabase(context);
        database.open();
        currentTrackID = database.getLastTrackID() + 1;
        locMgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        locLstnr = new CustomLocationListener();
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLstnr);
    }


    public class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc){
            String text = "Adding location : " +
                    "Latitude = " + loc.getLatitude() +
                    "Longitude = " + loc.getLongitude();
            Log.d("SERVICE_DEBUG", text);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

            long returnValue = database.addLocation(currentTrackID, String.valueOf(loc.getLatitude()),
                                String.valueOf(loc.getLongitude()));
            Toast.makeText(context, "Returned DB value: " + returnValue, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider){
            Log.d("SERVICE_DEBUG", "GPS is disabled");
        }

        @Override
        public void onProviderEnabled(String provider){
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
