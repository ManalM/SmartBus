package com.example.smartbus.driver;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.smartbus.interfaces.LatLngInterpolator;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
//todo:delete from driver and use in student
public class MarkerAnimationHelper {
      public void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
         final LatLng startPosition = marker.getPosition();
         final long start = SystemClock.uptimeMillis();
         final AccelerateDecelerateInterpolator interpolator =new  AccelerateDecelerateInterpolator();
         final float durationInMs = 2000f;
          Handler handler = new Handler();
          handler.post(new Runnable() {
             int elapsed = 0;
             int  t= 0;
             float v= 0;
             @Override
             public void run() {
                 elapsed = (int) (SystemClock.uptimeMillis() - start);
                 t = (int) (elapsed / durationInMs);
                 v = (int) interpolator.getInterpolation(t);
                 marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                 // Repeat till progress is complete.
                 if (t < 1) {
                     // Post again 16ms later.
                     new Handler().postDelayed(this, 16);
             }
         }


              });

};

}