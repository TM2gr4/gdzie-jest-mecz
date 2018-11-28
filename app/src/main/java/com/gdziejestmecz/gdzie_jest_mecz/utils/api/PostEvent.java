package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import android.os.AsyncTask;
import android.util.Log;

import com.gdziejestmecz.gdzie_jest_mecz.models.Event;

public class PostEvent extends AsyncTask<String, Void, Boolean> {
    public AsyncEventListResponse delegate = null;
    private Event event;

    public PostEvent(Event event) {
        this.event = event;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        return Api.postEvent(event);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("API_CALL", "Request DONE");
        delegate.addEventProcessFinished(success);
    }

}
