package com.tarija.tresdos.tarijasegura.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.model.Notification;
import com.tarija.tresdos.tarijasegura.model.myreponse;
import com.tarija.tresdos.tarijasegura.model.sender;
import com.tarija.tresdos.tarijasegura.other.common;
import com.tarija.tresdos.tarijasegura.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseUser user;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Huid = "HuidKey";
    public String tokenP;
    ApiService mService;

    public EmergencyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mService = common.getFCMClient();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 15) {
            guardarE();
        }
    }

    private void guardarE() {
        final String texto = sharedpreferences.getString(Huid,"");
        HijosRef.child("tokenP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tokenP = dataSnapshot.getValue(String.class);
                HijosRef.child("hijos").child(texto).child("nombre").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HijosRef.child("hijos").child(texto).child("alerta").setValue("si");
                        String NombreH = dataSnapshot.getValue(String.class);
                        Notification notification = new Notification("Tarija Segura: ",NombreH +" ha activado una alerta");
                        sender sender = new sender(tokenP, notification);
                        mService.SendNotification(sender)
                                .enqueue(new Callback<myreponse>() {
                                    @Override
                                    public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                        if (response.body().success == 1){
                                            showNotification("Tus familiares seran notificados");
                                        }
                                        else{
                                            showNotification("Error...");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<myreponse> call, Throwable t) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showNotification(String Text) {
        final NotificationManager mgr = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setContentTitle("Tarija Segura");
        note.setContentText(Text);
        note.setContentInfo("Enviando alerta...");
        note.setAutoCancel(true);
        note.setSmallIcon(R.mipmap.ic_launcher);
        mgr.notify(101, note.build());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
