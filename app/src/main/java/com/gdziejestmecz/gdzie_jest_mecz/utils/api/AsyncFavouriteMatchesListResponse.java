package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import java.util.ArrayList;

public interface AsyncFavouriteMatchesListResponse {
    void retrieveFavouriteMatchesProcessFinished(ArrayList<Match> matchList);
}
