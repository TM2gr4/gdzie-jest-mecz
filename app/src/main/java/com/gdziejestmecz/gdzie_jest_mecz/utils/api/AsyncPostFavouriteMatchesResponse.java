package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public interface AsyncPostFavouriteMatchesResponse {
    void postFavouriteMatches(Match match);
}
