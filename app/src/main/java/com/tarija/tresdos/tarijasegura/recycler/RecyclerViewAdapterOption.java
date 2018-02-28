package com.tarija.tresdos.tarijasegura.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.tarija.tresdos.tarijasegura.other.OptionClass;

import java.util.List;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewAdapterOption extends RecyclerView.Adapter<RecyclerViewHoldersOption> {

    private List<OptionClass> itemList;
    private Context context;

    public RecyclerViewAdapterOption(Context context, List<OptionClass> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersOption onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, null);
        RecyclerViewHoldersOption rcv = new RecyclerViewHoldersOption(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersOption holder, int position) {
        holder.ImgIcon.setImageResource(itemList.get(position).getIcon());
        holder.txtOption.setText(itemList.get(position).getName());
        holder.txtDescription.setText(itemList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
