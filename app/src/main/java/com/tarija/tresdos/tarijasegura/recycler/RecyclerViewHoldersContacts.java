package com.tarija.tresdos.tarijasegura.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewHoldersContacts extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUrlName, create_at, tvInformId;
    public Button btnGo, btnDelete;
    public ItemClickListerner itemClickListerner;

    public RecyclerViewHoldersContacts(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        txtUrlName = (TextView) itemView.findViewById(R.id.txtUrlName);
        tvInformId = (TextView) itemView.findViewById(R.id.tvInformId);
        create_at = (TextView) itemView.findViewById(R.id.create_at);
        btnGo = (Button) itemView.findViewById(R.id.btnGo);
        btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
    }

    public void setItemClickListerner(ItemClickListerner itemClickListerner) {
        this.itemClickListerner = itemClickListerner;
    }

    @Override
    public void onClick(View v) {
        itemClickListerner.onClik(v, getAdapterPosition(), false);
    }
}
