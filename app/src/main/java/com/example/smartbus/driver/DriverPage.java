package com.example.smartbus.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartbus.R;

import java.util.ArrayList;

import com.example.smartbus.SigninActivity;
import com.example.smartbus.driver.ListAdapter;
import com.google.android.material.navigation.NavigationView;

public class DriverPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private  RecyclerView recyclerView;
    private String[] students;
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

        recyclerView = findViewById(R.id.recycler_view);
       students = new String[3];
        students[0]="Dhai";
        students[1]="Fatima";
        students[2]="Anwar";


       // getSupportActionBar().setTitle("Students");

        recyclerView.setLayoutManager(new GridLayoutManager(DriverPage.this, 1));
        ListAdapter listAdapter = new ListAdapter(DriverPage.this, students);
        recyclerView.setAdapter(listAdapter);


    /*    listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });*/

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
                startActivity(new Intent(DriverPage.this, EditDriverProfile.class));

                break;

            case R.id.nav_logout_driver:
                startActivity(new Intent(DriverPage.this, SigninActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
