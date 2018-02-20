package com.tarija.tresdos.tarijasegura.remote;



import com.tarija.tresdos.tarijasegura.model.myreponse;
import com.tarija.tresdos.tarijasegura.model.sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA_FeQ6Mw:APA91bHUSs_WURI8iIm9uQhUV4Mw3Nrx-RDTidWcuDBEst8bFSlIfwD4ZSSj11y58K00B0ByWJs4OPMG1bp13NUIcR3r96AokGC-iznlFPROQ4d2cAiSe8GpFPcxkg0QIJfl5dGzPdub"
    })
    @POST("fcm/send")
    Call<myreponse> SendNotification(@Body sender body);
}
