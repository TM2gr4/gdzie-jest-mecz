package com.gdziejestmecz.gdzie_jest_mecz.components.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.MatchData;

import java.util.ArrayList;

public interface AsyncMatchListResponse {
    void retrieveMatchesProcessFinished(ArrayList<MatchData> matchList);
}
