package com.tarija.tresdos.tarijasegura.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.model.Notification;
import com.tarija.tresdos.tarijasegura.model.myreponse;
import com.tarija.tresdos.tarijasegura.model.sender;
import com.tarija.tresdos.tarijasegura.other.browser;
import com.tarija.tresdos.tarijasegura.other.common;
import com.tarija.tresdos.tarijasegura.remote.ApiService;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.everything.providers.android.browser.Bookmark;
import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.android.browser.Search;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowserService extends Service {

    private BrowserProvider browserProvider;
    private List<Bookmark> bookmarks;
    private List<Search> searches;
    private FirebaseUser user;
    private DatabaseReference rootRef,childRef;
    public String tokenP;
    List<String> listanegra = new ArrayList<String>(Arrays.asList(
            "xxx",
            "gore",
            "hentai",
            "xvideos.com",
            "porndish.com",
            "petardas.com",
            "pornografia",
            "desnudas",
            "putas",
            "asesinatos",
            "drogas"));
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String UltimoNotificado = "ultimoKey";
    public static final String Huid = "HuidKey";
    ApiService mService;

    public BrowserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        browserProvider = new BrowserProvider(this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mService = common.getFCMClient();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UltimoNotificado, "prueba");
        editor.commit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child(user.getUid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int sdk = Build.VERSION.SDK_INT;
                if ( sdk < 23){
                    bookmarks = browserProvider.getBookmarks().getList();
                    searches = browserProvider.getSearches().getList();
                    final String ultimo = bookmarks.get(bookmarks.size() - 1).title;
                    final int visitas = bookmarks.get(bookmarks.size() - 1).visits;
                    final String url = bookmarks.get(bookmarks.size() - 1).url;
                    final Long creado = bookmarks.get(bookmarks.size() - 1).created;
                    for (int v = 0; v < listanegra.size(); v++){
                        String Valores = listanegra.get(v);
                        String texto = sharedpreferences.getString(UltimoNotificado,"");
                        if (ultimo.contains(Valores)){
                            if (!ultimo.contains(texto)){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(UltimoNotificado, ultimo);
                                editor.commit();
                                rootRef.child(user.getUid()).child("tokenP").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (sharedpreferences.contains(Huid)) {
                                            String t =sharedpreferences.getString(Huid, "");
//                                            bookmarks.get(v).date;
                                            Log.d("Token Hijo", "onDataChange: " + t);
                                            Log.d("DatosSitio", "Titulo: "+ ultimo + " Url: "+ url + " Cantidad Visitas: "+ visitas + " Creado: " + creado);
                                            browser nuevo = new browser(ultimo,url, visitas, creado, new Date().toString());
                                            childRef.child("hijos").child(t).child("historial").push().setValue(nuevo);
                                        }
                                        else{
                                            Log.d("Token Hijo", "No Existe Token");
                                        }
                                        tokenP = dataSnapshot.getValue(String.class);
                                        EnviarNot(tokenP);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
        },2000, 2000);
        return START_STICKY;
    }

    private void EnviarNot(final String tokenP) {
        String texto = sharedpreferences.getString(Huid,"");
        childRef.child("hijos").child(texto).child("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NombreH = dataSnapshot.getValue(String.class);
                Notification notification = new Notification("Tarija Segura",NombreH +" observa contenido inapropiado");
                sender sender = new sender(tokenP, notification);
                mService.SendNotification(sender)
                        .enqueue(new Callback<myreponse>() {
                            @Override
                            public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                if (response.body().success == 1){
                                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Estas buscando contenido inapropiado", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                    mdToast.show();
                                }
                                else{
                                    Log.d("Error", "Error");
                                }
                            }

                            @Override
                            public void onFailure(Call<myreponse> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
