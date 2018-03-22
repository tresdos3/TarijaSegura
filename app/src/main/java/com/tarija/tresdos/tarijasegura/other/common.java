package com.tarija.tresdos.tarijasegura.other;


import com.tarija.tresdos.tarijasegura.remote.ApiService;
import com.tarija.tresdos.tarijasegura.remote.retrofitClient;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class common {
    public  static String fcmtoken = "AAAA_FeQ6Mw:APA91bHUSs_WURI8iIm9uQhUV4Mw3Nrx-RDTidWcuDBEst8bFSlIfwD4ZSSj11y58K00B0ByWJs4OPMG1bp13NUIcR3r96AokGC-iznlFPROQ4d2cAiSe8GpFPcxkg0QIJfl5dGzPdub";
    private static String baseurl = "https://fcm.googleapis.com/";
    public  static ApiService getFCMClient(){
        return retrofitClient.getClient(baseurl).create(ApiService.class);
    }
}
