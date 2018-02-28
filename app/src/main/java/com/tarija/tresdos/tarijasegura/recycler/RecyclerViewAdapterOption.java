package com.tarija.tresdos.tarijasegura.recycler;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarija.tresdos.tarijasegura.MainActivity;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.fragments.AlertFragment;
import com.tarija.tresdos.tarijasegura.fragments.ContactFragment;
import com.tarija.tresdos.tarijasegura.fragments.HistoryFragment;
import com.tarija.tresdos.tarijasegura.fragments.LocationFragment;
import com.tarija.tresdos.tarijasegura.interfaces.ItemClickListerner;
import com.tarija.tresdos.tarijasegura.other.ChildClass;
import com.tarija.tresdos.tarijasegura.other.OptionClass;
import com.valdesekamdem.library.mdtoast.MDToast;

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
        holder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClik(View view, int position, boolean isLongClick) {
                FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (itemList.get(position).getType()){
                    case "ubicar":
                        ft.addToBackStack(null);
                        ft.replace(R.id.content, new LocationFragment());
                        ft.commit();
                        break;
                    case "historia":
                        ft.addToBackStack(null);
                        ft.replace(R.id.content, new HistoryFragment());
                        ft.commit();
                        break;
                    case "contacto":
                        ft.addToBackStack(null);
                        ft.replace(R.id.content, new ContactFragment());
                        ft.commit();
                        break;
                    case "alerta":
                        ft.addToBackStack(null);
                        ft.replace(R.id.content, new AlertFragment());
                        ft.commit();
                        break;
                    case "bloquear":
                        MDToast.makeText(view.getContext(), "Aun trabajamos en esta opcion :)", MDToast.TYPE_INFO).show();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
