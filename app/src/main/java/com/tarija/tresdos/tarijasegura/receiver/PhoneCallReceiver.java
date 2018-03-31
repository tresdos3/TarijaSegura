package com.tarija.tresdos.tarijasegura.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.model.Notification;
import com.tarija.tresdos.tarijasegura.model.myreponse;
import com.tarija.tresdos.tarijasegura.model.sender;
import com.tarija.tresdos.tarijasegura.other.common;
import com.tarija.tresdos.tarijasegura.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tresdos on 11-07-17.
 */

public class PhoneCallReceiver extends BroadcastReceiver {

    private DatabaseReference rootRef,HijosRef;
    private FirebaseUser user;
    public String phoneNumber;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Huid = "HuidKey";
    ApiService mService;
    public String tokenP;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
            {
                sharedpreferences = context.getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                mService = common.getFCMClient();
                user = FirebaseAuth.getInstance().getCurrentUser();
                rootRef = FirebaseDatabase.getInstance().getReference();
                HijosRef = rootRef.child(user.getUid());
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
                    TelephonyManager tmgr = (TelephonyManager) context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    Bundle bundle = intent.getExtras();
                    phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    final String texto = sharedpreferences.getString(Huid,"");
                    HijosRef.child("tokenP").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tokenP = dataSnapshot.getValue(String.class);
                            HijosRef.child("hijos").child(texto).child("nombre").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String NombreH = dataSnapshot.getValue(String.class);
                                    Notification notification = new Notification("Tarija Segura informa... ",NombreH +" llamada del num: "+phoneNumber);
                                    sender sender = new sender(tokenP, notification);
                                    mService.SendNotification(sender)
                                            .enqueue(new Callback<myreponse>() {
                                                @Override
                                                public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                                    if (response.body().success == 1){
                                                        Log.d("AAA", "Notificacion enviada");
                                                    }
                                                    else{
                                                        Log.d("AAA", "Notificacion no enviada");
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
            }
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }
}
