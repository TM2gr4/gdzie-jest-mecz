package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public class PostFavouriteMatch extends AsyncTask<String, Void, Match> {
    public AsyncPostFavouriteMatchesResponse delegate = null;

    private Match match;

    public PostFavouriteMatch(Match match) {
        this.match = match;
    }
    @Override
    protected Match doInBackground(String... strings) {
        return Api.postFavoriteMatch(match);
    }

    @Override
    protected void onPostExecute(Match match) {
        super.onPostExecute(match);
        delegate.postFavouriteMatches(match);
    }

}
