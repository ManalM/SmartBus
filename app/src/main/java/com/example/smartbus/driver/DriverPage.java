package com.example.smartbus.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbus.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.smartbus.SigninActivity;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.tracking.helper.GoogleMapHelper;
import com.example.smartbus.tracking.helper.MarkerAnimationHelper;
import com.example.smartbus.tracking.helper.UiHelper;
import com.example.smartbus.tracking.model.Driver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DriverPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    public ListAdapter listAdapter;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200;

    private static FusedLocationProviderClient locationProviderClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;

    private UiHelper uiHelper;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_page);

        //-------------------Drawer and Toolbar-----------------
        Toolbar toolbar = findViewById(R.id.driver_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.driver_drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
        navUsername.setText(SharedPrefManager.getInstance(this).getUsername().substring(1));
        navigationView.setNavigationItemSelectedListener(this);
        //---------------------recyclerView-----------------------

        //connect with xml
        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<String> spase = new ArrayList<>();
        listAdapter = new ListAdapter(DriverPage.this, spase);
        recyclerView.setLayoutManager(new GridLayoutManager(DriverPage.this, 1));

        DataParser https = new DataParser(DriverPage.this, Constants.getStudentUrl, recyclerView, spase, listAdapter, "id_driver");
        https.execute();

        //-----------get driver location--------------
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uiHelper = new UiHelper();
        createLocationCallback();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = uiHelper.getLocationRequest();
        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(DriverPage.this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        } else requestLocationUpdate();

    }

    //------------------drawer function -----------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_profile_Driver:
                startActivity(new Intent(DriverPage.this, EditDriverProfile.class).putExtra("name", SharedPrefManager.getInstance(this).getUsername()));

                break;

            case R.id.nav_logout_driver:
                startActivity(new Intent(DriverPage.this, SigninActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---------------------- get location of driver function -----------------

    private void requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        if (uiHelper.isLocationProviderEnabled(this))
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
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
        locationCallback  = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LatLng latLng;
                if (locationResult.getLastLocation() ==null ) return;

                latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                Log.e("Location", latLng.latitude + " , " + latLng.longitude);
                Toast.makeText(DriverPage.this, latLng.latitude + " , " + latLng.longitude, Toast.LENGTH_SHORT).show();
           /*     if (locationFlag) {
                    locationFlag = false;
                }*/

                databaseReference.child("ONLINE_DRIVERS").child("Drivers").setValue(new Driver(latLng.latitude,latLng.longitude,SharedPrefManager.getInstance(DriverPage.this).getUsername()));



            }


        };
    }

    //-------------------------------------------------------------------------
    /// --------------------- retrieve  data inner class ----------------------------------
    //-------------------------------------------------------------------------


    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String url;
        RecyclerView recyclerView;

        ProgressDialog progressDialog;
        ArrayList<String> spase;
        String userid;
        ListAdapter listAdapter;

        public DataParser(Context c, String dataUrl, RecyclerView lv, ArrayList<String> arrayList, ListAdapter l, String user) {
            this.c = c;
            this.url = dataUrl;
            this.recyclerView = lv;
            userid = user;
            listAdapter = l;
            spase = arrayList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Retrieving data");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return this.getData(url, userid);
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            if (b == 1) {

                listAdapter = new ListAdapter(c, spase);
                recyclerView.setAdapter(listAdapter);
                recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(c, "toast", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        private String downloadData(String urlAddres, String userID) {
            InputStream is = null;
            String line = null;


            try {

                URL url = new URL(urlAddres);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                String userId = SharedPrefManager.getInstance(c).getUsername();


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter(userID, userId);

                String query = builder.build().getEncodedQuery();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                is = new BufferedInputStream(httpURLConnection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));


                StringBuilder stringBuilder = new StringBuilder();
                if (stringBuilder != null) {


                    while ((line = bufferedReader.readLine()) != null) {

                        stringBuilder.append(line + "\n");

                    }
                } else {
                    return null;
                }

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        //-- reading json code-----
        private int getData(String urlAddres, String userID) {
            String jsonData = downloadData(urlAddres, userID);
            try {

                JSONArray ja = new JSONArray(jsonData);

                JSONObject jo = null;

                spase.clear();
                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    String name = jo.getString("child_name");

                    spase.add(name);
                }

                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }
}
