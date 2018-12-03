package com.gdziejestmecz.gdzie_jest_mecz.utils;

import android.location.Address;

import java.util.List;

public interface AsyncLocationFinderResponse {
    void onLocationSeachCompleted(List<Address> addressList);
}
