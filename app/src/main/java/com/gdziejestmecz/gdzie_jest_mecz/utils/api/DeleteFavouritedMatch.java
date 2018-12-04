package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public class DeleteFavouritedMatch extends AsyncTask<String, Void, Match> {
    public AsyncDeleteFavouritedMatchesListResponse delegate = null;

    private Match match;

    public DeleteFavouritedMatch(Match match) {
        this.match = match;
    }
    @Override
    protected Match doInBackground(String... strings) {
        return Api.deleteFavouritedMatch(match);
    }

    @Override
    protected void onPostExecute(Match match) {
        super.onPostExecute(match);
        delegate.retrieveDeleteFavouritedMatchesProcessFinished(match);
    }

}
