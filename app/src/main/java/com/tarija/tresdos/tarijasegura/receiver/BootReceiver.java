package com.tarija.tresdos.tarijasegura.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.tarija.tresdos.tarijasegura.service.BrowserService;
import com.tarija.tresdos.tarijasegura.service.ContactsService;
import com.tarija.tresdos.tarijasegura.service.DetectAppService;
import com.tarija.tresdos.tarijasegura.service.EmergencyService;
import com.tarija.tresdos.tarijasegura.service.LocationService;
import com.valdesekamdem.library.mdtoast.MDToast;

/**
 * Created by Tresdos on 3/5/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        String type = sharedPreferences.getString(Tipo, "");
        if (!type.equals("p")){
            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent intentInternet = new Intent(context, ContactsService.class);
                context.startService(intentInternet);
                Intent intentBrowser = new Intent(context, BrowserService.class);
                context.startService(intentBrowser);
                Intent intentApps = new Intent(context, DetectAppService.class);
                context.startService(intentApps);
                Intent intentEmer = new Intent(context, EmergencyService.class);
                context.startService(intentEmer);
                Intent intentGeo = new Intent(context, LocationService.class);
                context.startService(intentGeo);

                MDToast.makeText(context, "Tarija Segura: Iniciando modulos :)", MDToast.TYPE_SUCCESS).show();
            }
        }
        else {
            MDToast.makeText(context, "Tarija Segura: Ejecutando...", MDToast.TYPE_SUCCESS).show();
        }
    }
}
