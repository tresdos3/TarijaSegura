package com.tarija.tresdos.tarijasegura.recycler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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
import com.tarija.tresdos.tarijasegura.other.EmergencyClass;
import com.tarija.tresdos.tarijasegura.other.OptionClass;

import java.util.List;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewAdapterEmergency extends RecyclerView.Adapter<RecyclerViewHoldersEmergency> {

    private List<EmergencyClass> itemList;
    private Context context;

    public RecyclerViewAdapterEmergency(Context context, List<EmergencyClass> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersEmergency onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_item, null);
        RecyclerViewHoldersEmergency rcv = new RecyclerViewHoldersEmergency(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersEmergency holder, int position) {
        holder.ImgIcon.setImageResource(itemList.get(position).getIcon());
        holder.txtOption.setText(itemList.get(position).getName());
        holder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClik(View view, int position, boolean isLongClick) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+itemList.get(position).getNumber()));

                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
