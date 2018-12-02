package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

public class PostGoogleToken extends AsyncTask<String, Void, GoogleTokenResponse> {
    public AsyncPostGoogleTokenResponse delegate = null;


    private final String token;
    private final TokenType tokenType;

    public PostGoogleToken(String token, TokenType tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    @Override
    protected GoogleTokenResponse doInBackground(String... strings) {
        return Api.postGoogleToken(token, tokenType);
    }

    @Override
    protected void onPostExecute(GoogleTokenResponse success) {
        super.onPostExecute(success);
        Log.d("API_CALL", "Request DONE");
        delegate.postGoogleTokenProcessFinished(success);
    }

}
