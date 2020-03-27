package com.example.smartbus.driver;

import com.example.smartbus.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapHelper {
        private final int ZOOM_LEVEL = 18;
        private final int TILT_LEVEL = 25;

    public CameraUpdate buildCameraUpdate(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .tilt(25)
                .zoom(18)
                .build();
        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

    public MarkerOptions getDriverMarkerOptions(LatLng position){
        MarkerOptions options = getMarkerOptions(R.drawable.bus, position);
        options.flat(true);
        return options;
    }
    private MarkerOptions getMarkerOptions( int resource, LatLng  position) {
        return new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(resource))
                .position(position);
    }
}
