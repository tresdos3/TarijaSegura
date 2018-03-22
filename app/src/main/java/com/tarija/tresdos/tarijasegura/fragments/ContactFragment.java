package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.tarija.tresdos.tarijasegura.other.contact;
import com.tarija.tresdos.tarijasegura.recycler.RecyclerViewAdapterContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ChildListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager lLayout;
    private RecyclerView rView;

    private View mView;

    private ConstraintLayout child_content2, child_content3;
    private RelativeLayout child_content1;

    private FirebaseUser user;
    private DatabaseReference rootRef, HijosRef;

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Child = "child_id";
    private String Key;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        child_content1 = (RelativeLayout) mView.findViewById(R.id.child_content1);
        child_content2 = (ConstraintLayout) mView.findViewById(R.id.child_content2);
        child_content3 = (ConstraintLayout) mView.findViewById(R.id.child_content3);

        sharedPreferences = this.getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        Key = sharedPreferences.getString(Child, "");
        viewLoading();
        getAllContacts();

        lLayout = new LinearLayoutManager(getContext());
        rView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        return mView;
    }

    private void getAllContacts() {
        final List<contact> allItems = new ArrayList<contact>();
        HijosRef.child("hijos").child(Key).child("contactos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    contact con = postSnapshot.getValue(contact.class);
                    allItems.add(new contact(
                            postSnapshot.getKey(),
                            con.getNombre(),
                            con.getNumero()
                    ));
                }
                List<contact> rowListItem = allItems;
                if(rowListItem.size() > 0){
                    RecyclerViewAdapterContacts rcAdapter = new RecyclerViewAdapterContacts(getContext(), rowListItem);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
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
