package com.tarija.tresdos.tarijasegura.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarija.tresdos.tarijasegura.helper.ILocationConstants;
import com.tarija.tresdos.tarijasegura.storage.AppPreferences;
import com.tarija.tresdos.tarijasegura.storage.IPreferenceConstants;

import java.text.DateFormat;
import java.util.Date;

import static com.tarija.tresdos.tarijasegura.storage.IPreferenceConstants.PREF_DISTANCE;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ILocationConstants, IPreferenceConstants {

    private static final String TAG = LocationService.class.getSimpleName();
    private long UPDATE_INTERVAL = 5000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime;
    private Location oldLocation;
    private Location newLocation;
    private AppPreferences appPreferences;
    private float distance;
    SharedPreferences sharedpreferences;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    public static final String mypreference = "mypref";
    public static final String Huid = "HuidKey";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        appPreferences = new AppPreferences(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");

        mLastUpdateTime = "";

        distance = appPreferences.getFloat(PREF_DISTANCE, 0);

        Log.d(TAG, "onCreate Distance: " + distance);
        String texto = sharedpreferences.getString(Huid,"");
//        maps nuevo = new maps(location.getLatitude(),location.getLongitude());
        HijosRef.child("hijos").child(texto).child("ubicacion").child("latitud").setValue("0");
        HijosRef.child("hijos").child(texto).child("ubicacion").child("longitud").setValue("0");
        HijosRef.child("hijos").child(texto).child("ubicacion").child("ultima").setValue("0");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        buildGoogleApiClient();

        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        return START_STICKY;

    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (SecurityException ex) {


        }
    }

    private void updateUI() {

        if (null != mCurrentLocation) {

            StringBuilder sbLocationData = new StringBuilder();
            sbLocationData
                    .append("Latitud")
                    .append(mCurrentLocation.getLatitude())
                    .append("\n")
                    .append("Longitud")
                    .append(mCurrentLocation.getLongitude())
                    .append("\n")
                    .append("Ultima Localisacion")
                    .append(mLastUpdateTime)
                    .append("\n")
                    .append("Distancia")
                    .append(getUpdatedDistance())
                    .append(" meters");

            FirebaseUser user = auth.getCurrentUser();
            rootRef = FirebaseDatabase.getInstance().getReference();
            HijosRef = rootRef.child(user.getUid());
            String texto = sharedpreferences.getString(Huid,"");
            HijosRef.child("hijos").child(texto).child("ubicacion").child("latitud").setValue(mCurrentLocation.getLatitude());
            HijosRef.child("hijos").child(texto).child("ubicacion").child("longitud").setValue(mCurrentLocation.getLongitude());
            HijosRef.child("hijos").child(texto).child("ubicacion").child("ultima").setValue(mLastUpdateTime);
            appPreferences.putFloat(PREF_DISTANCE, distance);

            Log.d(TAG, "Location Data:\n" + sbLocationData.toString());

            sendLocationBroadcast(sbLocationData.toString());
        } else {

            Toast.makeText(this, "No se pudo obtener tu localizacion", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLocationBroadcast(String sbLocationData) {

        Intent locationIntent = new Intent();
        locationIntent.setAction(LOACTION_ACTION);
        locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);

        LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
        protected void stopLocationUpdates() {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    @Override
    public void onDestroy() {

        appPreferences.putFloat(PREF_DISTANCE, distance);

        stopLocationUpdates();

        mGoogleApiClient.disconnect();

        Log.d(TAG, "onDestroy Distance " + distance);


        super.onDestroy();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.i(TAG, "Connected to GoogleApiClient");


        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        startLocationUpdates();

    }
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    private float getUpdatedDistance() {
        if (mCurrentLocation.getAccuracy() > ACCURACY_THRESHOLD) {

            return distance;
        }


        if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0) {

            oldLocation.setLatitude(mCurrentLocation.getLatitude());
            oldLocation.setLongitude(mCurrentLocation.getLongitude());

            newLocation.setLatitude(mCurrentLocation.getLatitude());
            newLocation.setLongitude(mCurrentLocation.getLongitude());

            return distance;
        } else {

            oldLocation.setLatitude(newLocation.getLatitude());
            oldLocation.setLongitude(newLocation.getLongitude());

            newLocation.setLatitude(mCurrentLocation.getLatitude());
            newLocation.setLongitude(mCurrentLocation.getLongitude());

        }


        /**
         * Calculate distance between last two geo locations
         */
        distance += newLocation.distanceTo(oldLocation);

        return distance;
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
