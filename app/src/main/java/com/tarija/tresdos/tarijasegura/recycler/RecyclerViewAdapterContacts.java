package com.tarija.tresdos.tarijasegura.recycler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.browser;
import com.tarija.tresdos.tarijasegura.other.contact;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Date;
import java.util.List;

/**
 * Created by Tresdos on 2/25/2018.
 */

public class RecyclerViewAdapterContacts extends RecyclerView.Adapter<RecyclerViewHoldersContacts> {

    private List<contact> itemList;
    private Context context;

    public RecyclerViewAdapterContacts(Context context, List<contact> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersContacts onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, null);
        RecyclerViewHoldersContacts rcv = new RecyclerViewHoldersContacts(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersContacts holder, final int position) {
        holder.txtUrlName.setText(itemList.get(position).getNombre());
        holder.create_at.setText(itemList.get(position).getNumero());
        holder.tvInformId.setText("Contacto");
        holder.btnGo.setText("LLamar...");
        holder.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + itemList.get(position).getNumero()));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
            }
        });
        holder.btnDelete.setText("Whatsapp...");
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toNumber = itemList.get(position).getNumero();
                boolean isWhatsappInstalled = estaInstalado("com.whatsapp");
                if (isWhatsappInstalled) {
                    toNumber = toNumber.replace("+", "").replace(" ", "");
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, ""+ itemList.get(position).getNombre());
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
                else {
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(goToMarket);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    private boolean estaInstalado(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
