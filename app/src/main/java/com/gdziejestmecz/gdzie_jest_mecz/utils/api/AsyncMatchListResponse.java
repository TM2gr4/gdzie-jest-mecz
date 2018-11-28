package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Event;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import java.util.ArrayList;

public interface AsyncMatchListResponse {
    void retrieveMatchesProcessFinished(ArrayList<Event> matchList);
}
