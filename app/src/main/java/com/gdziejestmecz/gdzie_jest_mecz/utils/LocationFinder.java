package com.gdziejestmecz.gdzie_jest_mecz.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

public class LocationFinder extends AsyncTask<Object, List<Address>, List<Address>> {

    private AsyncLocationFinderResponse asyncLocationFinderResponse;

    public LocationFinder(AsyncLocationFinderResponse asyncLocationFinderInterface){
        asyncLocationFinderResponse = asyncLocationFinderInterface;
    }

    @Override
    protected List<Address> doInBackground(Object... objects) {
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder((Context)objects[0]);
        try {
            addressList = geocoder.getFromLocationName((String)objects[1], 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList;
    }

    @Override
    protected void onPostExecute(List<Address> addressList) {
        asyncLocationFinderResponse.onLocationSeachCompleted(addressList);

        /*
        Address address;
        if (addressList != null && addressList.size() > 0) {
            address = addressList.get(0);
            mapViewFragment.drawMarker(new LatLng(address.getLatitude(), address.getLongitude()), location);
        } else {
            Toast.makeText(getApplicationContext(), "Nie znaleziono podanego miejsca", Toast.LENGTH_SHORT).show();
        }*/
    }
}
