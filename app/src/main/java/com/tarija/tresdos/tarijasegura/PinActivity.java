package com.tarija.tresdos.tarijasegura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.valdesekamdem.library.mdtoast.MDToast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PinActivity extends AppCompatActivity {

    private PinLockView mPinLockView, mPinLockView2;
    private IndicatorDots mIndicatorDots, mIndicatorDots2;
    private final static String TAG = PinActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Password = "passwordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView2 = (PinLockView) findViewById(R.id.pin_lock_view2);
        mIndicatorDots2 = (IndicatorDots) findViewById(R.id.indicator_dots2);
        mPinLockView2.setVisibility(View.INVISIBLE);
        mIndicatorDots2.setVisibility(View.INVISIBLE);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(6);

        mPinLockView2.attachIndicatorDots(mIndicatorDots2);
        mPinLockView2.setPinLength(6);

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(final String pin) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Confirma tu pin", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                mPinLockView.setVisibility(View.INVISIBLE);
                mIndicatorDots.setVisibility(View.INVISIBLE);
                mPinLockView2.setVisibility(View.VISIBLE);
                mIndicatorDots2.setVisibility(View.VISIBLE);
                mPinLockView2.setPinLockListener(new PinLockListener() {
                    @Override
                    public void onComplete(String pin2) {
                        if (pin.equals(pin2)){
                            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Pin Aceptado...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                            mdToast.show();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Password, pin);
                            editor.commit();
                            finish();
                            goMainActivity();
                        }
                        else {
                            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Los pin no son iguales", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                            mdToast.show();
                            final Intent intent = getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);
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
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            messages(MDToast.TYPE_SUCCESS, "Ha cancelado su solicitud...");
                            goLoginScreen();
                        } else {
                            messages(MDToast.TYPE_ERROR, "Error al cerrar sesion");
                        }
                    }
                });
    }
    public void messages(int type, String message){
        MDToast.makeText(getApplicationContext(), message, MDToast.LENGTH_SHORT, type).show();
    }
    private void goLoginScreen() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Estas seguro?")
                .setContentText("Aun no terminaste tu registro!")
                .setConfirmText("Si estoy seguro!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        signOut();
                        goLoginScreen();
                    }
                })
                .setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
