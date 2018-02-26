package com.tarija.tresdos.tarijasegura.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarija.tresdos.tarijasegura.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildListFragment extends Fragment {


    public ChildListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_list, container, false);
        // Inflate the layout for this fragment
        return view;
    }

}
