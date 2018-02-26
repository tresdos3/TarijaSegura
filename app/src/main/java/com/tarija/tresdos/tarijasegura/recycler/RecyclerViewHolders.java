package com.tarija.tresdos.tarijasegura.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tarija.tresdos.tarijasegura.R;

import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView TextViewNameMatter;
    public ItemClickListerner itemClickListerner;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        TextViewNameMatter = (TextView) itemView.findViewById(R.id.TextViewNameMatter);
    }

    public void setItemClickListerner(ItemClickListerner itemClickListerner) {
        this.itemClickListerner = itemClickListerner;
    }

    @Override
    public void onClick(View v) {
        itemClickListerner.onClik(v, getAdapterPosition(), false);
    }
}
