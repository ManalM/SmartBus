package com.example.smartbus.driver;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbus.R;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.tracking.helper.GoogleMapHelper;
import com.example.smartbus.tracking.helper.MarkerAnimationHelper;
import com.example.smartbus.tracking.model.Driver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.smartbus.driver.UiHelper;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class StudentLocation extends AppCompatActivity  implements OnMapReadyCallback{

    private static final String MAP_VIEW_BUNDLE_KEY ="map_key" ;
    //-----
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200;

    private static GoogleMap gMap;
    private static FusedLocationProviderClient locationProviderClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;
/*    private boolean locationFlag = true;
    private boolean driverOnlineFlag = true;
    private Marker currentPositionMarker = null;*/
    private GoogleMapHelper googleMapHelper ;
    private MarkerAnimationHelper markerAnimationHelper ;
    private UiHelper uiHelper = new UiHelper();
    MapView mapView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_location);
         mapView = findViewById(R.id.location);
        markerAnimationHelper = new MarkerAnimationHelper();
        //firebaseHelper = new FirebaseHelper("Drivers");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        googleMapHelper = new GoogleMapHelper();
        // -------------------mapView-----------------
        Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
        mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
    }

          mapView.onCreate(mapViewBundle);

       //
          mapView.getMapAsync(this);

        createLocationCallback();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = uiHelper.getLocationRequest();
        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(StudentLocation.this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        } else requestLocationUpdate();

/*        driverStatusTextView = findViewById < TextView > (R.id.driverStatusTextView)
                findViewById < SwitchCompat > (R.id.driverStatusSwitch).setOnCheckedChangeListener {
            _, b ->
                    driverOnlineFlag = b
            if (driverOnlineFlag)
                driverStatusTextView.text = resources.getString(R.string.online_driver)
            else {
                driverStatusTextView.text = resources.getString(R.string.offline)
                firebaseHelper.deleteDriver();
            }
        }*/

    }

    private void requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        if (uiHelper.isLocationProviderEnabled(this))
       /*     uiHelper.showPositiveDialogWithListener(this, resources.getString(R.string.need_location), resources.getString(R.string.location_content), object : IPositiveNegativeListener {
            override fun onPositive() {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }, "Turn On", false);*/
      // showPositiveDialogWithListener(this,"need your location","are you agree to access your location","ok",true);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

/*    private void showOrAnimateMarker(LatLng latLng) {
        if (currentPositionMarker == null)
            currentPositionMarker = gMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng));
        else
            markerAnimationHelper.animateMarkerToGB(currentPositionMarker, latLng, new LatLngInterpolator.Spherical());
    }*/

/*
    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(latLng);
        gMap.animateCamera(cameraUpdate, 10, null);
    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            int value = grantResults[0];
            if (value == PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PERMISSION_GRANTED) requestLocationUpdate();
        }
    }

    private void createLocationCallback() {
        locationCallback  = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LatLng latLng;
                if (locationResult.getLastLocation() ==null ) return;

                latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                Log.e("Location", latLng.latitude + " , " + latLng.longitude);
                Toast.makeText(StudentLocation.this, latLng.latitude + " , " + latLng.longitude, Toast.LENGTH_SHORT).show();
           /*     if (locationFlag) {
                    locationFlag = false;
                }*/

                databaseReference.child("ONLINE_DRIVERS").child("Drivers").setValue(new Driver(latLng.latitude,latLng.longitude,SharedPrefManager.getInstance(StudentLocation.this).getUsername()));



            }


        };
    }

    public void showPositiveDialogWithListener(Context callingClassContext, String title, String content, String positiveText, Boolean cancelable) {
        AlertDialog.Builder buildDialog = new AlertDialog.Builder(callingClassContext);
        buildDialog.setTitle(title);
        buildDialog.setMessage(content);
        buildDialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });
         buildDialog.setCancelable(cancelable)
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       gMap = googleMap;
        gMap.setMinZoomPreference(12);
        gMap.setIndoorEnabled(true);
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        LatLng ny = new LatLng(24.160247, 47.272907);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gMap.addMarker(markerOptions);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny,18));

  /*      locationCallback  = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LatLng latLng;
                if (locationResult == null) return;

                latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                Log.e("Location", latLng.latitude + " , " + latLng.longitude);
                if (locationFlag) {
                    locationFlag = false;
                    animateCamera(latLng);
                }
                if (driverOnlineFlag)
                    firebaseHelper.updateDriver(new Driver(latLng.latitude, latLng.longitude));
                showOrAnimateMarker(latLng);
            }


        };
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());*/
}


}
