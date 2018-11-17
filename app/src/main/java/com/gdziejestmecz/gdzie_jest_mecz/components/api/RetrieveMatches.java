package com.gdziejestmecz.gdzie_jest_mecz.components.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.Event;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;
import com.gdziejestmecz.gdzie_jest_mecz.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RetrieveMatches extends AsyncTask<String, Void, ArrayList<Match>> {
    public AsyncMatchListResponse delegate = null;

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {

        ArrayList<Match> matches = getMatches();

        return matches;
    }

    @Override
    protected void onPostExecute(ArrayList<Match> matchData) {
        super.onPostExecute(matchData);
        Log.d("API_CALL", "Request RETRIEVE MATCHES DONE");
        delegate.retrieveMatchesProcessFinished(matchData);
    }

    private ArrayList<Match> getMatches(){
        ArrayList<Match> matches = new ArrayList<Match>();
        try {

            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointMatches());

            JSONArray jsonArray;
            jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int id = obj.getInt("id");
                String date = obj.getString("date");
                String time = obj.getString("time");

                Team homeTeam = new Team(obj.getInt("id"), "RKS Chuwdu", "http://dupa.pl");
                Team awayTeam = new Team(obj.getInt("id"), "JBC Falubas", "http://dupa.pl");

                Match match = new Match(id, homeTeam, awayTeam, date, time);

                matches.add(match);
            }

            Log.d("API_CALL", "got matches: " + result);
            return matches;
        }catch(JSONException e) {
            Log.d("JSON", "could parse matches, because: " + e.getMessage());
        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for matches, because: " + e.getMessage());
        }

        return matches;
    }
}
