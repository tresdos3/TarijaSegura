package com.tarija.tresdos.tarijasegura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

public class SelectChildActivity extends AppCompatActivity {

    private TextView titleSe;
    private Button btnSeleccionar;
    private DatabaseReference rootRef,HijosRef;
    private List<String> listaHijos = new ArrayList<>();
    private List<String> listaUID = new ArrayList<>();
    private MaterialSpinner ListaSpiner;
    private SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Session = "SessionKey";
    public static final String Huid = "HuidKey";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_child);

        titleSe = (TextView) findViewById(R.id.titleSelec);
        ListaSpiner = (MaterialSpinner) findViewById(R.id.ListaHijos);
        btnSeleccionar = (Button) findViewById(R.id.btnSeleccionar);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        rootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        HijosRef = rootRef.child(user.getUid());
        GetHijos();
        Log.d("aaa", "onCreate: esto llego aqui");

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Seleccionado: " + ListaSpiner.getText(), MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                mdToast.show();
                int v = ListaSpiner.getSelectedIndex();
                HijosRef.child("hijos").child(listaUID.get(v)).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                HijosRef.child("hijos").child(listaUID.get(v)).child("estado").setValue("si");
                HijosRef.child("hijos").child(listaUID.get(v)).child("alerta").setValue("no");
                HijosRef.child("hijos").child(listaUID.get(v)).child("SDK").setValue(Build.VERSION.SDK_INT);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Session, "si");
                editor.putString(Huid, listaUID.get(v));
                editor.commit();
                Intent i = new Intent(SelectChildActivity.this, PinActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    private void GetHijos() {
        HijosRef.child("hijos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaHijos.clear();
                listaUID.clear();
                listaUID.add("null");
                listaHijos.add("Seleccionar...");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    ChildClass hijo = postSnapshot.getValue(ChildClass.class);
                    if (hijo.getEstado().equals("no")){
                        listaUID.add(postSnapshot.getKey());
                        listaHijos.add(hijo.getNombre());
                    }
                }
                ListaSpiner.setItems(listaHijos);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Error al conectar con la base de datos", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
            }
        });
    }

}
