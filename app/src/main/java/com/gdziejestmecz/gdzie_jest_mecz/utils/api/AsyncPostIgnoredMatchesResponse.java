package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

public interface AsyncPostIgnoredMatchesResponse {
    void postIgnoredMatchProcessFinished(Match match);
}
