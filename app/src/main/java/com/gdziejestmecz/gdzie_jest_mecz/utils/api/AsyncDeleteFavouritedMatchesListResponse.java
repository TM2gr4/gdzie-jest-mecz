package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public interface AsyncDeleteFavouritedMatchesListResponse {
    void retrieveDeleteFavouritedMatchesProcessFinished(Match match);
}
