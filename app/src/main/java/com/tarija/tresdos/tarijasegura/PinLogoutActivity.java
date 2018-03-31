package com.tarija.tresdos.tarijasegura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.tarija.tresdos.tarijasegura.other.PolicyManager;
import com.tarija.tresdos.tarijasegura.other.common;
import com.tarija.tresdos.tarijasegura.remote.ApiService;
import com.tarija.tresdos.tarijasegura.service.BrowserService;
import com.tarija.tresdos.tarijasegura.service.ContactsService;
import com.tarija.tresdos.tarijasegura.service.DetectAppService;
import com.tarija.tresdos.tarijasegura.service.EmergencyService;
import com.tarija.tresdos.tarijasegura.service.LocationService;
import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinLogoutActivity extends AppCompatActivity {

    private PolicyManager policyManager;

    private DatabaseReference rootRef,HijosRef, HijoEstado;
    private FirebaseUser user;

    public String tokenP;
    ApiService mService;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private final static String TAG = PinLogoutActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    //    tipo p=padre h=hijo n=ninguno
    public static final String Password = "passwordKey";
    public static  final String NombreHIJO = "Nombrehijo";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";
    public static final String Huid = "HuidKey";
    public static final String BtnVisible = "btnKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_logout);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        policyManager = new PolicyManager(this);
        mService = common.getFCMClient();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());

        mPinLockView = (PinLockView) findViewById(R.id.Lpin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.Lindicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(6);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                String texto = sharedpreferences.getString(Password,"");
                String texto2 = sharedpreferences.getString(Huid,"");
                if (pin.equals(texto)) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Pin Aceptado...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                    signOut();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();

                    HijoEstado = rootRef.child(user.getUid()).child("hijos").child(texto2).child("estado");
                    HijoEstado.setValue("no");
                    policyManager.disableAdmin();
                    finish();
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:com.tarija.tresdos.tarijasegura"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Pin Incorrecto", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    HijosRef.child("hijos").child(texto2).child("nombre").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String NombreH = dataSnapshot.getValue(String.class);
                            Notification notification = new Notification("Tarija Segura",NombreH +" ha intentado quitar la app");
                            sender sender = new sender(tokenP, notification);
                            mService.SendNotification(sender)
                                    .enqueue(new Callback<myreponse>() {
                                        @Override
                                        public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Enviando notificacion", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                                mdToast.show();

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
            }

            @Override
            public void onEmpty() {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Error! no ingresaste un pin", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
            }
        });
    }
    private void cerrarServicio(){
        Intent intentGeo = new Intent(this, LocationService.class);
        this.stopService(intentGeo);
        Intent intentEmer = new Intent(this, EmergencyService.class);
        this.stopService(intentEmer);
        Intent intentInternet = new Intent(this, ContactsService.class);
        this.stopService(intentInternet);
        Intent intentApps = new Intent(this, DetectAppService.class);
        this.stopService(intentApps);
        Intent intentBrowser = new Intent(this, BrowserService.class);
        this.stopService(intentBrowser);
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            messages(MDToast.TYPE_SUCCESS, "Ha terminado su sesion");
                            cerrarServicio();
                            goLogInScreen();
                        } else {
                            messages(MDToast.TYPE_ERROR, "Error al cerrar sesion");
                        }
                    }
                });
    }
    public void messages(int type, String message){
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, type).show();
    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}
