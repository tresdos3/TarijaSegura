package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tarija.tresdos.tarijasegura.R;

import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private MapView mapView;
    private View mView;
    private FirebaseUser user;
    private DatabaseReference rootRef,HijosRef;
    private TextView result;
    private Button btnhab;
    private String Key;
    public double Lat, Long;

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Child = "child_id";

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location, container, false);
        result = (TextView) mView.findViewById(R.id.result);
        btnhab = (Button) mView.findViewById(R.id.btnhab);
        sharedPreferences = this.getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        Key = sharedPreferences.getString(Child, "");
        HijosRef.child("hijos").child(Key).child("alerta").setValue("no");
        btnhab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Estas segura/o?")
                        .setContentText("Estas enviando una notificacion...!")
                        .setConfirmText("Si, lo estoy!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
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
        });
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;

        HijosRef.child("hijos").child(Key).child("ubicacion").child("latitud").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lat = dataSnapshot.getValue(Double.class);
                HijosRef.child("hijos").child(Key).child("ubicacion").child("longitud").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long = dataSnapshot.getValue(Double.class);
                        LatLng sydney = new LatLng(Lat,Long);
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions()
                                .anchor(0.0f, 1.0f)
                                .title("Esta aqui!")
                                .snippet("Tu hijo se encuentra aqui...")
                                .position(sydney));
                        CameraPosition l = CameraPosition.builder().target(sydney)
                                .zoom(16)
                                .bearing(0)
                                .tilt(45)
                                .build();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(l));

                        Geocoder geocoder = new Geocoder(getContext());
                        try {

                            List<Address> addressList = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 15);
                            if (addressList != null && addressList.size() > 0) {
                                String locality = addressList.get(0).getAddressLine(0);
                                String country = addressList.get(0).getLocality();
                                if (!locality.isEmpty() && !country.isEmpty()) {
                                    result.setText(locality + " - " + country);
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.addMarker(
//                new MarkerOptions()
//                        .position(new LatLng(40.689247, -74.044502))
//                        .title("Hola")
//                        .snippet("Este es un test")
//        );
//        CameraPosition l = CameraPosition.builder().target(new LatLng(40.689247, -74.044502))
//                .zoom(16)
//                .bearing(0)
//                .tilt(45)
//                .build();
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(l));
    }

}
