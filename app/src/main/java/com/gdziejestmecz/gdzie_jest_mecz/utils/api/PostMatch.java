package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public class PostMatch extends AsyncTask<String, Void, Boolean> {
    public AsyncAddPostListResponse delegate = null;
    private Match match;

    public PostMatch(Match match) {
        this.match = match;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        return Api.postMatch(match);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("API_CALL", "Request DONE");
        delegate.addMatchProcessFinished(success);
    }

}
