package com.example.smartbus.driver;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseHelper {


    private String driverId;

    private final String ONLINE_DRIVERS = "online_drivers";
    DatabaseReference onlineDriverDatabaseReference;
    public FirebaseHelper(String driverId) {
        this.driverId = driverId;
        onlineDriverDatabaseReference =FirebaseDatabase.getInstance().getReference().child(ONLINE_DRIVERS).child(driverId);

    }

    private void init() {
        onlineDriverDatabaseReference
                .onDisconnect()
                .removeValue();
    }

    public void updateDriver(Driver driver) {
        onlineDriverDatabaseReference
                .setValue(driver);
        Log.e("Driver Info", " Updated");
    }

    private void deleteDriver() {
        onlineDriverDatabaseReference
                .removeValue();
    }
}
