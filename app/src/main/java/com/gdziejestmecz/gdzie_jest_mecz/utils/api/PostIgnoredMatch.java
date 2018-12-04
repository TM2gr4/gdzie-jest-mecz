package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

public class PostIgnoredMatch extends AsyncTask<String, Void, Match> {
    public AsyncPostIgnoredMatchesResponse delegate = null;

    private Match match;

    public PostIgnoredMatch(Match match) {
        this.match = match;
    }
    @Override
    protected Match doInBackground(String... strings) {
        return Api.postIgnoredMatch(match);
    }

    @Override
    protected void onPostExecute(Match match) {
        super.onPostExecute(match);
        delegate.postIgnoredMatchProcessFinished(match);
    }

}
