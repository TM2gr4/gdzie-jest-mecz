package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import java.util.ArrayList;

public interface AsyncIgnoredMatchesListResponse {
    void retrieveIgnoredMatchesProcessFinished(ArrayList<Match> matchList);
}
