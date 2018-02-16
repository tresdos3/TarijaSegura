package com.tarija.tresdos.tarijasegura;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tarija.tresdos.tarijasegura.other.PersonClass;
import com.valdesekamdem.library.mdtoast.MDToast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileConfActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText name, lastname, email, ci;
    private MaterialSpinner family;
    private Button btnRegister, btnUpdate;
    private AwesomeValidation awesomeValidation;

    private DatabaseReference rootRef, profileRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_conf);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        name = (TextInputEditText) findViewById(R.id.name_padre);
        lastname = (TextInputEditText) findViewById(R.id.lastname_padre);
        email = (TextInputEditText) findViewById(R.id.email_padre);
        ci = (TextInputEditText) findViewById(R.id.ci_padre);
        family = (MaterialSpinner) findViewById(R.id.select_padre);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        final SweetAlertDialog pDialog = new SweetAlertDialog(ProfileConfActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Buscando usuario...");
        pDialog.setCancelable(true);
        pDialog.show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        profileRef = rootRef.child(user.getUid());

        if (user == null){
            goLoginScreen();
        }
        else {
            profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        profileRef.child("profile").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                name.setText(dataSnapshot.child("name").getValue().toString());
                                lastname.setText(dataSnapshot.child("lastname").getValue().toString());
                                email.setText(dataSnapshot.child("email").getValue().toString());
                                ci.setText(dataSnapshot.child("ci").getValue().toString());
                                family.setText(dataSnapshot.child("family").getValue().toString());
                                pDialog.dismissWithAnimation();
                                messages(MDToast.TYPE_SUCCESS,"Datos cargados :)");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                pDialog.dismissWithAnimation();
                            }
                        });
                        btnRegister.setEnabled(false);
                    }
                    else {
                        pDialog.dismissWithAnimation();
                        btnUpdate.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    messages(MDToast.TYPE_ERROR,"Esto es vergonzoso :)");
                }
            });
        }



        family.setItems("Selecciona una opcion...","Abuelo (a)", "Hermano (a)", "Padre", "Madre", "Tio (a)");
        awesomeValidation.addValidation(this, R.id.name_padre,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.lastname_padre,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lastnameerror);
        awesomeValidation.addValidation(this, R.id.email_padre, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.ci_padre, "^(0|[1-9][0-9]*)$", R.string.cierror);
        btnRegister.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                ValidateForm();
                break;
            case R.id.btnUpdate:
                ValidateForm();
                break;
        }
    }
    public void ValidateForm(){
        if (awesomeValidation.validate()) {
            if (!family.getText().equals("Selecciona una opcion...")) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(ProfileConfActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Espere por favor...");
                pDialog.setCancelable(true);
                pDialog.show();
                PersonClass person = new PersonClass(name.getText().toString(), lastname.getText().toString(), email.getText().toString(), ci.getText().toString(), family.getText().toString());
                profileRef.child("/profile").setValue(person)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(ProfileConfActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Felicidades ya puede :)!")
                                        .setContentText("Continuar...!")
                                        .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();

                                            }
                                        })
                                        .show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(ProfileConfActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error al registrar!")
                                        .setContentText("Reintentar...!")
                                        .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                goMainActivity();
                                            }
                                        })
                                        .show();
                            }
                        });
            }
            else {
                messages(MDToast.TYPE_ERROR,"Seleccione un opcion...");
            }
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
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
}
