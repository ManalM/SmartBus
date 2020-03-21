package com.example.smartbus.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.smartbus.driver.ListAdapter;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.RecyclerViewHttps;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.server.https;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DriverPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    public static ListAdapter listAdapter;

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
        navigationView.setNavigationItemSelectedListener(this);
        //---------------------recyclerView-----------------------

        //connect with xml
        recyclerView = findViewById(R.id.recycler_view);


        RecyclerViewHttps https = new RecyclerViewHttps(DriverPage.this, Constants.getStudentUrl, recyclerView, "id_driver");
        https.execute();

        recyclerView.setLayoutManager(new GridLayoutManager(DriverPage.this, 1));


    }

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
                startActivity(new Intent(DriverPage.this, EditDriverProfile.class).putExtra("name", "mohammed"));
                //todo:put name of driver from database instead
                break;

            case R.id.nav_logout_driver:
                startActivity(new Intent(DriverPage.this, SigninActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-------------------------------------------------------------------------
    /// --------------------- retrieve  data ----------------------------------
    //-------------------------------------------------------------------------


    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String jsonData;
        RecyclerView recyclerView;

        ProgressDialog progressDialog;
        ArrayList<String> spase = new ArrayList<>();

        public DataParser(Context c, String jsonData, RecyclerView lv) {
            this.c = c;
            this.jsonData = jsonData;
            this.recyclerView = lv;
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
            return this.getData();
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            if (b == 1) {

                listAdapter = new ListAdapter(c, spase);
                recyclerView.setAdapter(listAdapter);

            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }


        //-- reading json code-----
        private int getData() {

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
