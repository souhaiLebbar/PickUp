package com.example.rustybucket.pickup;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by brand on 3/3/2018.
 */

public class LocManager {
    private static final int LOCATION_REFRESH_TIME = 10;
    private static final int LOCATION_REFRESH_DISTANCE = 1;

    private LocationManager locationManager;
    private Geocoder geocoder;

    private final Context context;

    public LocManager(Context c) {
        context = c;
        geocoder = new Geocoder(context, Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION))) {
                //Requests Users Permission
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Task<List<String>> getZipcodeAtCurrentLocation() {

        final TaskCompletionSource<List<String>> tcs = new TaskCompletionSource<>();

        //Checks for permissions and requests if not available
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION))) {
                //Requests Users Permission
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSION_REQUEST_FINE_LOCATION);
                //TODO Make a toast or snackbar message to describe how this app will not function well without this permission allowed
                getZipcodeAtCurrentLocation();
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationManager.removeUpdates(this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        Set<String> s = new LinkedHashSet<>();
                        for (Address address: addresses) {
                            if (address.getPostalCode() != null) {
                                s.add(address.getPostalCode());
                            }
                        }
                        tcs.setResult(new ArrayList<>(s));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        //Builds zipcodes as a task and sets it in the location listener

        return tcs.getTask();
    }


}



