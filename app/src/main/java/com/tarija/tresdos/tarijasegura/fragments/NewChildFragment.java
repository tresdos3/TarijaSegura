package com.tarija.tresdos.tarijasegura.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarija.tresdos.tarijasegura.ProfileConfActivity;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.ChildClass;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChildFragment extends Fragment {

    private EditText txtNombre;
    private AppCompatButton btnRegistrar;
    private DatabaseReference rootRef,HijosRef;

    public NewChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_child, container, false);

        txtNombre = (EditText) view.findViewById(R.id.txtNombreH);
        btnRegistrar = (AppCompatButton) view.findViewById(R.id.btnRegistro);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateForm();
            }
        });
        return view;
    }
    public void ValidateForm() {
        if (txtNombre.getText().toString().trim().length() != 0){
            ChildClass nuevo = new ChildClass(txtNombre.getText().toString(), "null","no","no");
            final String id = HijosRef.child("hijos").push().getKey();
            HijosRef.child("hijos/"+id).setValue(nuevo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HijosRef.child("hijos/"+id +"/ubicacion/latitud").setValue(0);
                            HijosRef.child("hijos/"+id +"/ubicacion/longitud").setValue(0);
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Registrado!")
                                    .setContentText("Presiona para continuar!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack(null)
                                                    .replace(R.id.content, new DashboardFragment())
                                                    .commit();
                                        }
                                    })
                                    .show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error al Registrar!")
                                    .setContentText("Presiona para continuar!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    });

        }
    }
}
