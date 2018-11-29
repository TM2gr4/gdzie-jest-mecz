package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class RetrievePubs extends AsyncTask<String, Void, ArrayList<Pub>> {
    public AsyncPubListResponse delegate = null;

    @Override
    protected ArrayList<Pub> doInBackground(String... strings) {
        ArrayList<Pub> pubs = getPubs();
        return pubs;
    }

    @Override
    protected void onPostExecute(ArrayList<Pub> pubData) {
        super.onPostExecute(pubData);
        Log.d("API_CALL", "Request RETRIEVE PUBS DONE");
        delegate.retrievePubsProcessFinished(pubData);
    }

    private ArrayList<Pub> getPubs(){
        ArrayList<Pub> pubs = new ArrayList<Pub>();
        try {
            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointPubs());
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                int id = obj.getInt("id");
                double lat = obj.getDouble("latitude");
                double lon = obj.getDouble("longitude");
                String street = obj.getString("street");
                String number = obj.getString("number");
                String name = obj.getString("name");

                Pub pub = new Pub(id, lat, lon, name, street, number);
                pubs.add(pub);
            }
            Log.d("API_CALL", "got pubs: " + result);
            return pubs;
        }catch(JSONException e) {
            Log.d("JSON", "could parse pubs, because: " + e.getMessage());
        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for pubs, because: " + e.getMessage());
        }

        return pubs;
    }
}


