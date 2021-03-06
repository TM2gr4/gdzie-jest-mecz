package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;
import com.gdziejestmecz.gdzie_jest_mecz.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RetrieveIgnoredMatches extends AsyncTask<String, Void, ArrayList<Match>> {
    public AsyncIgnoredMatchesListResponse delegate = null;

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {
        ArrayList<Match> matches = getMatches();
        return matches;
    }

    @Override
    protected void onPostExecute(ArrayList<Match> matchData) {
        super.onPostExecute(matchData);
        Log.e("API_CALL", "Request RETRIEVE IGNORED MATCHES DONE");
        delegate.retrieveIgnoredMatchesProcessFinished(matchData);
    }

    private ArrayList<Match> getMatches(){
        ArrayList<Match> matches = new ArrayList<Match>();
        try {
            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointIgnoredMatches());
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int id = obj.getInt("id");
                String date = obj.getString("date");
                String time = obj.getString("time");

                JSONObject homeObj = obj.getJSONObject("homeTeam");
                Team homeTeam = new Team(homeObj.getInt("id"),
                                        homeObj.getString("name"),
                                        homeObj.getString("imgUrl"), homeObj.getString("countryOfOrigin"));

                JSONObject awayObj = obj.getJSONObject("awayTeam");
                Team awayTeam = new Team(awayObj.getInt("id"),
                                        awayObj.getString("name"),
                                        awayObj.getString("imgUrl"), awayObj.getString("countryOfOrigin"));

                ArrayList<Pub> pubs = new ArrayList<Pub>();
                JSONArray pubsJsonArray = obj.getJSONArray("pubs");
                for (int j = 0; j < pubsJsonArray.length(); j++) {
                    JSONObject placeObj = pubsJsonArray.getJSONObject(j);
                    int pubId = placeObj.getJSONObject("pub").getInt("id");
                    double lati = placeObj.getJSONObject("pub").getDouble("latitude");
                    double longi = placeObj.getJSONObject("pub").getDouble("longitude");
                    String street = placeObj.getJSONObject("pub").getString("street");
                    String number = placeObj.getJSONObject("pub").getString("number");
                    String name = placeObj.getJSONObject("pub").getString("name");
//                    String desc = placeObj.getString("desc");

                    Pub pub = new Pub(pubId, lati, longi, name, street, number);
                    pubs.add(pub);
                }

                Match match = new Match(id, homeTeam, awayTeam, date, time, pubs);
                matches.add(match);
            }
            Log.e("API_CALL", "got IGNORED MATCHES: " + result);
            return matches;
        }catch(JSONException e) {
            Log.e("JSON", "could parse IGNORED MATCHES, because: " + e.getMessage());
        }catch(IOException e) {
            Log.e("API_CALL", "could not fetch for IGNORED MATCHES, because: " + e.getMessage());
        }

        return matches;
    }
}
