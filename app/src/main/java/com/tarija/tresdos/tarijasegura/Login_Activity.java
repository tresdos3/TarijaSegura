package com.tarija.tresdos.tarijasegura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Arrays;

public class Login_Activity extends AppCompatActivity {

    private LinearLayout l1, l2;
    private Button btnSiguiente;
    private Animation uptodown, downtoup;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        btnSiguiente = (Button) findViewById(R.id.buttonsub);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        mRootView = findViewById(R.id.root);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 777) {
            handleSignInResponse(resultCode, data);
        }
    }
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == ResultCodes.OK) {
            goMainScreen();
        } else {
            if (response == null) {
                messages(MDToast.TYPE_ERROR,"Sesion Cancelada");
                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                messages(MDToast.TYPE_INFO,"Revisa tu conexion a internet");
                return;
            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                messages(MDToast.TYPE_INFO, "Error desconocido");
                return;
            }
        }
        messages(MDToast.TYPE_SUCCESS,"Espera un segundo...");
    }
    public void signInPhone(View view){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                        .build(),
                777);
    }
    public void goMainScreen(){
        Intent intent = new Intent(this, OptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
    public void messages(int type, String message){
        MDToast.makeText(mRootView.getContext(), message, MDToast.LENGTH_LONG, type).show();
    }
}
