package com.example.smartbus.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartbus.R;
import com.example.smartbus.SigninActivity;
import com.google.android.material.navigation.NavigationView;

public class DriverDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_drawer);

        Toolbar toolbar = findViewById(R.id.driver_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.driver_drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_profile_Driver:
                startActivity(new Intent(DriverDrawer.this, EditDriverProfile.class));

                break;

            case R.id.nav_logout_driver:
               startActivity(new Intent(DriverDrawer.this, SigninActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

