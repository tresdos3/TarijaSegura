package com.tarija.tresdos.tarijasegura.recycler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarija.tresdos.tarijasegura.MainActivity;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.fragments.OptionsFragment;
import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.tarija.tresdos.tarijasegura.other.browser;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewAdapterBrowser extends RecyclerView.Adapter<RecyclerViewHoldersBrowser> {

    private List<browser> itemList;
    private Context context;
    private FirebaseUser user;
    private DatabaseReference rootRef, HijosRef;


    public RecyclerViewAdapterBrowser(Context context, List<browser> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersBrowser onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, null);
        RecyclerViewHoldersBrowser rcv = new RecyclerViewHoldersBrowser(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersBrowser holder, final int position) {
        holder.txtUrlName.setText(itemList.get(position).getTitulo());
        long timestamp = itemList.get(position).getCreado();
        Date date = new Date(timestamp);
        holder.create_at.setText(date.toString());
        holder.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(position).getUrl()));
                context.startActivity(browserIntent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                rootRef = FirebaseDatabase.getInstance().getReference();
                HijosRef = rootRef.child("j9NozBQs7OU3qwUBgaIxCEsde1Y2");
                HijosRef.child(itemList.get(position).getKey()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                MDToast.makeText(context, "Eliminado :)", MDToast.TYPE_SUCCESS).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                MDToast.makeText(context, "Error al eliminar :(", MDToast.TYPE_WARNING).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

//    @Override
//    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
//        holder.TextViewNameMatter.setText(itemList.get(position).getNombre());
//        holder.setItemClickListerner(new ItemClickListerner() {
//            @Override
//            public void onClik(View view, int position, boolean isLongClick) {
//                FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.addToBackStack(null);
//                ft.replace(R.id.content, new OptionsFragment());
//                ft.commit();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.itemList.size();
//    }
}
