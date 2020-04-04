package com.example.smartbus.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.example.smartbus.tracking.helper.UiHelper;

import com.example.smartbus.tracking.helper.GoogleMapHelper;
import com.example.smartbus.tracking.helper.MarkerAnimationHelper;
import com.example.smartbus.tracking.interfaces.LatLngInterpolator;
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
import com.google.android.gms.maps.model.CameraPosition;
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

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class StudentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{
    private static final String ROOT_TEXT = "ONLINE_DRIVERS";
    private DrawerLayout drawer;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    //-----
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200;

    private static GoogleMap gMap;
    private static FusedLocationProviderClient locationProviderClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;
    private boolean locationFlag = true;
    private Marker currentPositionMarker = null;
    private GoogleMapHelper googleMapHelper;
    private MarkerAnimationHelper markerAnimationHelper;
    private UiHelper uiHelper = new UiHelper();
    DatabaseReference databaseReference;
    String nameOfStudent;
    TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        //-------------------Drawer and Toolbar-----------------
        state = findViewById(R.id.state);
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        googleMapHelper = new GoogleMapHelper();
        //-------------------mapView-------------------------
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.supportMap);
        fragment.getMapAsync(this);
        fragment.onCreate(mapViewBundle);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = uiHelper.getLocationRequest();


        //----------------------state text ----------------------
        databaseReference.child(ROOT_TEXT).child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("state").getValue(String.class);
                state.setText(s);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

/*
    private void requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        if (uiHelper.isLocationProviderEnabled(this))
*/
/*         uiHelper.showPositiveDialogWithListener(this, resources.getString(R.string.need_location), resources.getString(R.string.location_content), object : IPositiveNegativeListener {
            override fun onPositive() {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }, "Turn On", false);
            // showPositiveDialogWithListener(this,"need your location","are you agree to access your location","ok",true);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));*//*


        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
*/

    private void showOrAnimateMarker(LatLng latLng) {
        if (currentPositionMarker == null)
            currentPositionMarker = gMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng));
        else
            markerAnimationHelper.animateMarkerToGB(currentPositionMarker, latLng, new LatLngInterpolator.Spherical());
    }

    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(latLng);
        gMap.animateCamera(cameraUpdate, 10, null);
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            int value = grantResults[0];
            if (value == PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PERMISSION_GRANTED) requestLocationUpdate();
        }
    }*/





    //------------------------------display map function ---------------------------------


    @Override
    protected void onDestroy() {

        super.onDestroy();
        //locationProviderClient.removeLocationUpdates(locationCallback);
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
        LatLng ny = new LatLng(21.4916617, 39.2221246);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
           gMap.addMarker(markerOptions);

        databaseReference.child(ROOT_TEXT).child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    double lat = dataSnapshot.child("lat").getValue(Double.class);
                    double lng = dataSnapshot.child("lng").getValue(Double.class);
                    String driverId = dataSnapshot.child("driverId").getValue(String.class);
                    Driver driver = new Driver(lat, lng, driverId);
                    LatLng latLng = new LatLng(driver.lat, driver.lng);
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng);
                    showOrAnimateMarker(latLng);
        /*            Marker marker = gMap.addMarker(options);
                    marker.setTag(driver.driverId);*/
                    animateCamera(latLng);

                } else {
                    Toast.makeText(StudentPage.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
