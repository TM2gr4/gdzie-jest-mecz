package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

public class PostMatch extends AsyncTask<String, Void, Boolean> {
    public AsyncPostMatchResponse delegate = null;

    private final Pub pub;
    private final String description;

    private Match match;

    public PostMatch(Match match, Pub pub, String descripiton) {
        this.match = match;
        this.pub = pub;
        this.description = descripiton;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        return Api.postMatch(match, pub, description);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("API_CALL", "Request DONE");
        delegate.postMatchProcessFinished(success);
    }

}
