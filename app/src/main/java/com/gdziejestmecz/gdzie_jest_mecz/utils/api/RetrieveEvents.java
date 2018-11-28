package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.MainScreen;
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

public class RetrieveEvents extends AsyncTask<String, Void, ArrayList<Event>> {
    public MainScreen delegate = null;

    @Override
    protected ArrayList<Event> doInBackground(String... strings) {

        ArrayList<Event> events = getEvents();

        return events;
    }

    @Override
    protected void onPostExecute(ArrayList<Event> eventData) {
        super.onPostExecute(eventData);
        Log.d("API_CALL", "Request DONE");
        delegate.retrieveMatchesProcessFinished(eventData);
    }

    private Team getTeamById(int id){
        try {

            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointTeams());
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

    private ArrayList<Event> getEvents(){
        ArrayList<Event> events = new ArrayList<Event>();

        try {

            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointMatches());

            JSONArray jsonArray;
            jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int eventId = obj.getInt("id");
                int matchId = obj.getInt("matchId");
                int pubId = obj.getInt("pubId");
                double latitude = obj.getDouble("latitude");
                double longitude= obj.getDouble("longitude");
                String desc = obj.getString("description");

                Match match = getMatchById(matchId);
                Pub pub = getPubById(pubId);

                Event event = new Event(eventId, match, pub, 9999, latitude, longitude, desc);

                events.add(event);
            }

            Log.d("API_CALL", "got events: " + result);
            return events;
        }catch(JSONException e) {
            Log.d("JSON", "could parse events, because: " + e.getMessage());
        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for events, because: " + e.getMessage());
        }

        return events;
    }

    private Match getMatchById(int id) {
        try {
            String result = Api.get(ServerInfo.getRootUrl() + ServerInfo.getEndpointMatches() + "/" + 2);  // /id
            Log.d("API_CALL", result);

            try {
                JSONObject obj = new JSONObject(result);

                Team homeTeam = new Team(0, "Liverpool", "http:///dupa.com");
                Team awayTeam = new Team(1, "Real Madrid", "http:///dupa.com");

                int matchId = obj.getInt("id");
                String date = obj.getString("date");
                String time = obj.getString("time");//LocalDateTime.parse(obj.getString("time"));

                return new Match(matchId, homeTeam, awayTeam, date, time);

            }catch(JSONException e) {
                Log.d("JSON", "Couldn't parse incommming json because: " + e.getMessage());
            }

        }catch(IOException e) {
            Log.d("API_CALL", "could not fetch for match, because: " + e.getMessage());
        }
        return null;
    }

    private Pub getPubById (int id) {
        // tu bedzie api call po pub na podstawie ID, ale jak widać jeszcze nie ma
        return new Pub(id, "Pub biblioteka", "Struga 1");
    }
}
