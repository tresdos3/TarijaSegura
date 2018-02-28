package com.tarija.tresdos.tarijasegura.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewHoldersOption extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView ImgIcon;
    public TextView txtOption, txtDescription;
    public ItemClickListerner itemClickListerner;

    public RecyclerViewHoldersOption(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ImgIcon = (ImageView) itemView.findViewById(R.id.img);
        txtOption = (TextView) itemView.findViewById(R.id.txtOption);
        txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
    }

    public void setItemClickListerner(ItemClickListerner itemClickListerner) {
        this.itemClickListerner = itemClickListerner;
    }

    @Override
    public void onClick(View v) {
        itemClickListerner.onClik(v, getAdapterPosition(), false);
    }
}
