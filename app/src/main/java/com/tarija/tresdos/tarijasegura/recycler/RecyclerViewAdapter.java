package com.tarija.tresdos.tarijasegura.recycler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;
import com.tarija.tresdos.tarijasegura.other.ChildClass;

import java.util.List;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<ChildClass> itemList;
    private Context context;

//    private SharedPreferences sharedPreferences;
//    public static final String mypreference = "mypref";
//    public static final String Child = "child_id";
//    private SharedPreferences.Editor editor;

    public RecyclerViewAdapter(Context context, List<ChildClass> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.TextViewNameMatter.setText(itemList.get(position).getNombre());
        holder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClik(View view, int position, boolean isLongClick) {
                Toast.makeText(view.getContext(), "Key = " + itemList.get(position).getKey(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
