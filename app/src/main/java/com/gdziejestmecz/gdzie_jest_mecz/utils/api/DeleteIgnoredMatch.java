package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public class DeleteIgnoredMatch extends AsyncTask<String, Void, Match> {
    public AsyncDeleteIgnoredMatchesListResponse delegate = null;

    private Match match;

    public DeleteIgnoredMatch(Match match) {
        this.match = match;
    }
    @Override
    protected Match doInBackground(String... strings) {
        return Api.deleteIgnoredMatch(match);
    }

    @Override
    protected void onPostExecute(Match match) {
        super.onPostExecute(match);
        delegate.retrieveDeleteIgnoredMatchesProcessFinished(match);
    }

}
