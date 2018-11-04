package com.gdziejestmecz.gdzie_jest_mecz.components.api;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.MainScreen;
import com.gdziejestmecz.gdzie_jest_mecz.constants.ServerInfo;
import com.gdziejestmecz.gdzie_jest_mecz.models.MatchData;
import com.gdziejestmecz.gdzie_jest_mecz.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RetrieveEvents extends AsyncTask<String, Void, ArrayList<MatchData>> {
    @Override
    protected ArrayList<MatchData> doInBackground(String... strings) {
        try {
            String result = Api.run(ServerInfo.getRootUrl() + ServerInfo.getEndpointMatches());
            Log.d("API_CALL", result);

            ArrayList<MatchData> matches = new ArrayList<MatchData>();
            try {
                JSONArray jsonArray = null;
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //Team homeTeam = getTeamById(obj.getString('homeTeamId'));
                    Team homeTeam = new Team(0, "RKS Chuwudu", "http:///dupa.com");
                    Team awayTeam = new Team(1, "Polonia Napleton", "http:///dupa.com");

                    int matchId = Integer.parseInt(obj.getString("id"));
                    String date = obj.getString("date");
                    LocalDateTime time = LocalDateTime.now();//LocalDateTime.parse(obj.getString("time"));

                    MatchData matchData = new MatchData(matchId, homeTeam, awayTeam, date, time);

                    matches.add(matchData);
                }

                return matches;
            }catch(JSONException e) {
                Log.d("JSON", "Couldn't parse incommming json because: " + e.getMessage());
            }

        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for events, because: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MatchData> matchData) {
        super.onPostExecute(matchData);
        Log.d("API_CALL", "Request DONE");

    }

    private Team getTeamById(int id){
        try {

            String result = Api.run(ServerInfo.getRootUrl() + ServerInfo.getEndpointTeams());
            try{
                JSONObject obj = new JSONObject(result);
            }catch(JSONException e) {
                Log.d("JSON", "Couldn't parse incommming json because: " + e.getMessage());
            }

            Log.d("API_CALL", "got team from id " + id + ": " + result);
        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for team, because: " + e.getMessage());
        }

        return null;
    }
}
