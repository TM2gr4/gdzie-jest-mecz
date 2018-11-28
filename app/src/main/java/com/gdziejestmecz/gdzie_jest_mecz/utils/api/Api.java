package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean postMatch(Match match) {

        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("description", match.getDescription());
            jsonObject.put("id", Integer.toString(match.getId()));
//            jsonObject.put("longitude", Double.toString(match.getLongitude()));
//            jsonObject.put("matchId", Integer.toString(match.getMatch().getId()));
//            jsonObject.put("numberOfAttendees", Integer.toString(match.getNumberOfAttendees()));
//            jsonObject.put("pubId", Integer.toString(match.getPub().getId()));
//            jsonObject.put("pubId", Integer.toString(match.getPubs().getId()));

        }catch (JSONException e){
            Log.e("JSON", "Couldn't create json out of envt object. " + e.getMessage());
            return false;
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(ServerInfo.getRootUrl() + ServerInfo.getEndpointMatches() + ServerInfo.getAdd())
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 201 || response.code() == 200) {
                return true;
            } else {
                Log.e("API_CALL", "POSTING MATCH FAILED!!! BEACUSE response status code : " + response.code());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("API_CALL", "POSTING MATCH FAILED!!! BEACUSE: " + e.getMessage());
            return false;
        }
    }
}
