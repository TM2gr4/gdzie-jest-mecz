package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;

import android.util.Log;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

public class PostPub extends AsyncTask<String, Void, Boolean> {
    public AsyncPostPubResponse delegate = null;

    private final Pub pub;

    public PostPub(Pub pub){
        this.pub = pub;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return Api.postPub(pub);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("API_CALL", "Request DONE");
        delegate.postPubProcessFinished(success);
    }
}
