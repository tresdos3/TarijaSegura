package com.tarija.tresdos.tarijasegura.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BrowserService extends Service {
    public BrowserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
