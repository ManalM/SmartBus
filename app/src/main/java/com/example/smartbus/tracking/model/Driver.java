package com.example.smartbus.tracking.model;

import android.content.Context;

import com.example.smartbus.server.SharedPrefManager;

public class Driver {
  public   double lat ,lng;
 public String  driverId;


    public Driver(double lat, double lng, String driverId ){
    this.lat = lat;
    this.lng =lng;
    this.driverId = driverId;

   }

}
