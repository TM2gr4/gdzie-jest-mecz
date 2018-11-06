package com.gdziejestmecz.gdzie_jest_mecz.components.api;

import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.Event;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean postEvent(Event event) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("description", event.getDescription())
                    .addFormDataPart("id", Integer.toString(event.getId()))
                    .addFormDataPart("latitude", Double.toString(event.getLatiitude()))
                    .addFormDataPart("longitude", Double.toString(event.getLongitude()))
                    .addFormDataPart("matchId", Double.toString(event.getMatch().getId()))
                    .addFormDataPart("numberOfAttendees", Integer.toString(event.getNumberOfAttendees()))
                    .addFormDataPart("pubId", Integer.toString(event.getPub().getId()))
                    .build();

            Request request = new Request.Builder()
                    .url(ServerInfo.getRootUrl() + ServerInfo.getEndpointEvents() + ServerInfo.getAdd())
                    .post(requestBody)
                    .build();
            return true;
        } catch (Exception e) {
            Log.d("API_CALL", "POSTING EVENT FAILED!!! BEACUSE: " + e.getMessage());
            return false;
        }
    }
}
