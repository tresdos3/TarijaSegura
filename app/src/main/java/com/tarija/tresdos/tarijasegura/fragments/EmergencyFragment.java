package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.EmergencyClass;
import com.tarija.tresdos.tarijasegura.other.OptionClass;
import com.tarija.tresdos.tarijasegura.recycler.RecyclerViewAdapterEmergency;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment {

    private ChildListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager lLayout;
    private RecyclerView rView;
    private List<EmergencyClass> allItems;

    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);
        allItems = new ArrayList<EmergencyClass>();
        EmergencyClass a = new EmergencyClass(R.drawable.history, "Policia", "110");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history, "Bomberos", "119");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history, "Grupo Sar", "138");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history, "Emergencias", "118");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history,"Hospital San Juan de Dios","6645555");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history,"Hospital C.N.S.","6633610");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history,"Seguridad Ciudadana","6631001");
        allItems.add(a);
        a = new EmergencyClass(R.drawable.history,"Orden y Seguridad","6642222");
        allItems.add(a);

        lLayout = new LinearLayoutManager(getContext());
        rView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerViewAdapterEmergency rcAdapter = new RecyclerViewAdapterEmergency(getContext(), allItems);
        rView.setAdapter(rcAdapter);
        rView.setLayoutManager(lLayout);
        // Inflate the layout for this fragment
        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
