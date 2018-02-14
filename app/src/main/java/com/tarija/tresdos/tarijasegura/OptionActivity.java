package com.tarija.tresdos.tarijasegura;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdesekamdem.library.mdtoast.MDToast;

public class OptionActivity extends AppCompatActivity {

    private FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private Button btnPadre, btnHijo;

    //    tipo p=padre h=hijo n=ninguno
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        btnPadre = (Button) findViewById(R.id.btnpadre);
        btnHijo = (Button) findViewById(R.id.btnhijo);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            goLogInScreen();
        }

        btnPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Realizado!...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Tipo, "p");
                editor.commit();
                Intent i = new Intent(OptionActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnHijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Realizado!...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Tipo, "h");
                editor.commit();
                Intent i = new Intent(OptionActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}
