package com.gdziejestmecz.gdzie_jest_mecz.components.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Event;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import java.util.ArrayList;

public interface AsyncEventListResponse {
    void retrieveEventsProcessFinished(ArrayList<Event> eventList);
}
