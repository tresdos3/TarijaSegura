package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.tarija.tresdos.tarijasegura.other.OptionClass;
import com.tarija.tresdos.tarijasegura.recycler.RecyclerViewAdapterOption;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment {

    private ChildListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager lLayout;
    private RecyclerView rView;
    private List<OptionClass> allItems;

    public OptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        allItems = new ArrayList<OptionClass>();
        OptionClass a = new OptionClass(R.drawable.location, "Ubicar a mi hijo", "Ubica a su hijo y observe su ubicacion", "ubicar");
        allItems.add(a);
        a = new OptionClass(R.drawable.history, "Historial de navegacion", "Observe el historial de navegacion de su hijo", "historia");
        allItems.add(a);
        a = new OptionClass(R.drawable.contact, "Contactos de mi hijo", "Observe los contactos de su hijo", "contacto");
        allItems.add(a);
        a = new OptionClass(R.drawable.alert, "Enviar alerta", "Envie un mensaje alerta su hijo", "alerta");
        allItems.add(a);
        a = new OptionClass(R.drawable.ic_android_black_24dp, "Bloqueo de aplicaciones", "Bloquee apliaciones y limite su tiempo de uso", "bloquear");
        allItems.add(a);



        lLayout = new LinearLayoutManager(getContext());
        rView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        RecyclerViewAdapterOption rcAdapter = new RecyclerViewAdapterOption(getContext(), allItems);
        rView.setAdapter(rcAdapter);
        rView.setLayoutManager(lLayout);
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
