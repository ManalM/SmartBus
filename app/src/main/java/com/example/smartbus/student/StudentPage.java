package com.example.smartbus.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.SigninActivity;
import com.example.smartbus.driver.StudentLocation;
import com.example.smartbus.driver.UiHelper;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.tracking.FirebaseDriverListener;
import com.example.smartbus.tracking.helper.FirebaseEventListenerHelper;
import com.example.smartbus.tracking.helper.GoogleMapHelper;
import com.example.smartbus.tracking.helper.MarkerAnimationHelper;
import com.example.smartbus.tracking.model.Driver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class StudentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, FirebaseDriverListener {
    private DrawerLayout drawer;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    //-----
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200;

    private static GoogleMap gMap;
    private static FusedLocationProviderClient locationProviderClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;
    private boolean locationFlag = true;
    private boolean driverOnlineFlag = true;
    private Marker currentPositionMarker = null;
    private GoogleMapHelper googleMapHelper;
    private MarkerAnimationHelper markerAnimationHelper;
    private FirebaseEventListenerHelper valueEventListener;
    private UiHelper uiHelper = new UiHelper();
    DatabaseReference databaseReference;
    String nameOfStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        //-------------------Drawer and Toolbar-----------------
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        nameOfStudent = intent.getStringExtra("name");
        toolbar.setTitle(nameOfStudent);

        drawer = findViewById(R.id.student_drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view_student);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
        navUsername.setText(nameOfStudent);
        navigationView.setNavigationItemSelectedListener(this);
        // -------------------tracking----------------


        markerAnimationHelper = new MarkerAnimationHelper();
        //firebaseHelper = new FirebaseHelper("Drivers");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        googleMapHelper = new GoogleMapHelper();
        //-------------------mapView-------------------------
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        //mapView = findViewById(R.id.mapView);
        //mapView.onCreate(mapViewBundle);
        //mapView.getMapAsync(StudentPage.this);
      createLocationCallback();
       SupportMapFragment fragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.supportMap);
        fragment.getMapAsync(this);
        fragment.onCreate(mapViewBundle);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = uiHelper.getLocationRequest();
        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(StudentPage.this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        } else requestLocationUpdate();
/*        valueEventListener = new FirebaseEventListenerHelper(this);
        databaseReference.addChildEventListener(valueEventListener);*/
/*        databaseReference.child("ONLINE_DRIVERS").child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              //  for (DataSnapshot data :dataSnapshot.getChildren()){
                    Driver driver = dataSnapshot.getValue(Driver.class);
                LatLng latLng = new LatLng(driver.lat, driver.lng);
                    MarkerOptions markerOptions = googleMapHelper.getDriverMarkerOptions(latLng);
                    Marker marker = gMap.addMarker(markerOptions);
                    marker.setTag(driver.driverId);

                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        //Fragment fragment = (Fragment) findViewById(R.id.supportMap);
        //val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment
        //mapFragment.getMapAsync { googleMap = it }
    }
//-----------drawer function-----------------------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:

                startActivity(new Intent(StudentPage.this, EditStudentProfile.class).putExtra("name", nameOfStudent));

                break;
            case R.id.nav_payment:

                startActivity(new Intent(StudentPage.this, StudentPayment.class));

                break;
            case R.id.nav_feedback:

                startActivity(new Intent(StudentPage.this, DriverFeedback.class).putExtra("name", nameOfStudent));

                break;
            case R.id.nav_rate_driver:
                startActivity(new Intent(StudentPage.this, RateDriver.class).putExtra("name", nameOfStudent));
                break;
            case R.id.nav_logout_student:
                startActivity(new Intent(StudentPage.this, SigninActivity.class));
                break;
            case R.id.nav_list_student:
                startActivity(new Intent(StudentPage.this, StudentList.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//------------------------------tracking function------------------------

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

    private void showOrAnimateMarker(LatLng latLng) {
        if (currentPositionMarker == null)
            currentPositionMarker = gMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng));
        else
            markerAnimationHelper.animateMarkerToGB(currentPositionMarker, latLng, new com.example.smartbus.tracking.interfaces.LatLngInterpolator.Spherical());
    }

    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(latLng);
        gMap.animateCamera(cameraUpdate, 10, null);
    }

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
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LatLng latLng;
                if (locationResult.getLastLocation() == null) return;

                latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                Log.e("Location", latLng.latitude + " , " + latLng.longitude);
              //  Toast.makeText(StudentPage.this, latLng.latitude + " , " + latLng.longitude, Toast.LENGTH_SHORT).show();
                if (locationFlag) {
                    locationFlag = false;
                    animateCamera(latLng);

                    // databaseReference.child("ONLINE_DRIVERS").child("Drivers").setValue(new com.example.smartbus.driver.Driver(latLng.latitude,latLng.longitude,SharedPrefManager.getInstance(StudentLocation.this).getUsername()));

                }


            }


        };
    }

    @Override
    public void onDriverAdded(Driver driver) {
        // todo: delete the interface and make if driver click button put marker
  /*      MarkerOptions markerOptions = googleMapHelper.getDriverMarkerOptions(new LatLng(driver.lat, driver.lng));
        Marker marker = gMap.addMarker(markerOptions);
        marker.setTag(driver.driverId);*/
       /* MarkerCollection.insertMarker(marker);
        totalOnlineDrivers.text = resources.getString(R.string.total_online_drivers).plus(" ").plus(MarkerCollection.allMarkers().size)
*/
    }

    @Override
    public void onDriverRemoved(Driver driver) {
    }

    @Override
    public void onDriverUpdated(Driver driver) {

    }
    //------------------------------display map function ---------------------------------
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }


       // mapView.onSaveInstanceState(mapViewBundle);
    }

 /*   @Override
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
    }*/

    @Override
    protected void onDestroy() {
        //mapView.onDestroy();

        super.onDestroy();
       // databaseReference.removeEventListener(valueEventListener);
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

/*    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/

   @Override
    public void onMapReady(GoogleMap googleMap) {

       Toast.makeText(this, "onMapReady", Toast.LENGTH_SHORT).show();
        gMap = googleMap;
        gMap.setMinZoomPreference(12);
        gMap.setIndoorEnabled(true);
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        LatLng ny = new LatLng(21.4916617, 39.2221246);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
 //    gMap.addMarker(markerOptions);

        //  gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.161069, 47.272142), 18));
       databaseReference.child("ONLINE_DRIVERS").child("Drivers").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 for (DataSnapshot data :dataSnapshot.getChildren()){
            //  Driver driver = data.getValue(Driver.class);
                     //todo:errroooor
                     Toast.makeText(StudentPage.this, "for loop ", Toast.LENGTH_SHORT).show();
                     if (data.exists()) {
                         double lat = data.child("lat").getValue(Double.class);
                         double lng = data.child("lng").getValue(Double.class);
                         Driver driver = new Driver(lat, lng, "");
                         LatLng latLng = new LatLng(driver.lat, driver.lng);
                         Toast.makeText(StudentPage.this, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_SHORT).show();
                         MarkerOptions markerOptions = new MarkerOptions();
                         markerOptions.position(latLng);
                         gMap.addMarker(markerOptions);
                         // MarkerOptions markerOptions = googleMapHelper.getDriverMarkerOptions(latLng);
               /*Marker marker = gMap.addMarker(markerOptions);
               marker.setTag(driver.driverId);*/
                         animateCamera(latLng);
                     }else{
                         Toast.makeText(StudentPage.this, "null", Toast.LENGTH_SHORT).show();
                     }
              }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

}
