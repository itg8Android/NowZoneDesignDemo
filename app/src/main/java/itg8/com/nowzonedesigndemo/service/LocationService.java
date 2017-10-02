package itg8.com.nowzonedesigndemo.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationService extends Service {
    private static final long UPDATE_INTERVAL = 100 * 1000 ;
    private static final long FASTEST_INTERVAL = 2000;
    private static final String TAG = LocationService.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationProviderClient;
     private LocationRequest mLocationRequest;



    public LocationService() {
        checkDeviceVersion();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkDeviceVersion() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startLocationUpdates();
        } else {


        }
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    private void onLocationChanged(Location lastLocation) {
        String msg = "Updated Location: " +
                Double.toString(lastLocation.getLatitude()) + "," +
                Double.toString(lastLocation.getLongitude());
        Log.d(TAG,"Latitude"+lastLocation.getLatitude()+"Longitude"+lastLocation.getLongitude());
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    }


}

