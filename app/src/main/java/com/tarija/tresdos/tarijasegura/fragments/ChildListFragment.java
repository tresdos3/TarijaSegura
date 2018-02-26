package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.tarija.tresdos.tarijasegura.recycler.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager lLayout;
    private RecyclerView rView;

    private ConstraintLayout child_content2, child_content3;
    private RelativeLayout child_content1;

    private FirebaseUser user;
    private DatabaseReference rootRef, childRef;

    public ChildListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_list, container, false);

        child_content1 = (RelativeLayout) view.findViewById(R.id.child_content1);
        child_content2 = (ConstraintLayout) view.findViewById(R.id.child_content2);
        child_content3 = (ConstraintLayout) view.findViewById(R.id.child_content3);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        viewLoading();
        getAllChild();

        lLayout = new LinearLayoutManager(getContext());
        rView = (RecyclerView) view.findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        return view;
    }

    private void getAllChild() {
        final List<ChildClass> allItems = new ArrayList<ChildClass>();
        rootRef.child(user.getUid()).child("hijos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    ChildClass data = postSnapshot.getValue(ChildClass.class);
                    allItems.add(new ChildClass(
                            postSnapshot.getKey(),
                            "Nombre: " + data.getNombre(),
                            data.getToken(),
                            data.getEstado(),
                            data.getAlerta()
                    ));
                }
                List<ChildClass> rowListItem = allItems;
                if(rowListItem.size() > 0){
                    RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getContext(), rowListItem);
                    rView.setAdapter(rcAdapter);
                    viewContent();
                }
                else{
                    viewNodata();
                }
                Log.d("Datasss", "onComplete: "+ allItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public void viewLoading(){
        child_content1.setVisibility(View.GONE);
        child_content2.setVisibility(View.VISIBLE);
        child_content3.setVisibility(View.GONE);
    }
    public void viewNodata(){
        child_content1.setVisibility(View.GONE);
        child_content2.setVisibility(View.GONE);
        child_content3.setVisibility(View.VISIBLE);
    }
    public void viewContent(){
        child_content1.setVisibility(View.VISIBLE);
        child_content2.setVisibility(View.GONE);
        child_content3.setVisibility(View.GONE);
    }
}
