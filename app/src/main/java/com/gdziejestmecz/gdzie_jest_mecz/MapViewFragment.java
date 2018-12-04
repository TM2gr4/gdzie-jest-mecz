package com.gdziejestmecz.gdzie_jest_mecz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.pm.PackageManager;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapViewFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;

    private boolean mLocationPermissionGranted;
    private final int DEFAULT_ZOOM = 12;
    private boolean firstPositionUpdate = true;

    final static String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    final static int LOCATION = 1;
    final static int GPS_SETTINGS = 2;

    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if(firstPositionUpdate) {
                        firstPositionUpdate = false;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
                    }
                }
            }
        };

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                askForPermission(LOCATION);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {

        if (!mLocationPermissionGranted) {
            return;
        }
        Log.d("LOCATION","REQUESTING UPDATES");
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    public void drawMarker(LatLng location, String locationName, boolean doFocus) {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(location).title(locationName));

            if(doFocus)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,googleMap.getCameraPosition().zoom));
        }
    }

    public void clearMarkers(){
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    private void askForPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), INITIAL_PERMS[0]) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), INITIAL_PERMS[1]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(INITIAL_PERMS, requestCode);
        } else {
            mLocationPermissionGranted = true;
            askForGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("perm","perm results in");
        if(permissions.length>0) {
            Log.d("perm","perm results in 2");
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == LOCATION) {
                    mLocationPermissionGranted = true;
                    askForGPS();
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                mLocationPermissionGranted = false;
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void askForGPS(){

        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext()).addApi(LocationServices.API).build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    requestLocationUpdates();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(getActivity(),GPS_SETTINGS );
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GPS_SETTINGS){
            switch (resultCode){
                case Activity.RESULT_OK:
                {
                    requestLocationUpdates();
                    Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                    break;
                }
                case Activity.RESULT_CANCELED:
                {
                    Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
    }

}