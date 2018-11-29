package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

import java.util.ArrayList;

public interface AsyncPubListResponse {
    void retrievePubsProcessFinished(ArrayList<Pub> pubList);
}
