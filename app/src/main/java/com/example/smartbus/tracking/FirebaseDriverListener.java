package com.example.smartbus.tracking;


import com.example.smartbus.tracking.model.Driver;

public interface FirebaseDriverListener {

    public void onDriverAdded(Driver driver);

    public void onDriverRemoved(Driver driver);

    public void onDriverUpdated(Driver driver);
}
