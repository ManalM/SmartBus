package com.example.smartbus.tracking.interfaces;

import com.google.android.gms.maps.model.LatLng;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public  interface LatLngInterpolator {

    public LatLng interpolate(float fraction, LatLng a, LatLng b);
    public class Spherical implements    LatLngInterpolator  {

        private double computeAngleBetween( Double fromLat,  Double fromLng,  Double toLat,  Double toLng) {
            double dLat = fromLat - toLat;
            double dLng = fromLng - toLng;
            return 2 * Math.asin(sqrt(pow(sin(dLat / 2), 2.0) + cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2.0)));
        }

        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            // http://en.wikipedia.org/wiki/Slerp
            double fromLat = toRadians(a.latitude);
            double fromLng = toRadians(a.longitude);
            double toLat = toRadians(b.latitude);

            double toLng = toRadians(b.longitude);
            double cosFromLat = cos(fromLat);
            double cosToLat = cos(toLat);
            // Computes Spherical interpolation coefficients.
            double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
            double sinAngle = sin(angle);
            if (sinAngle < 1E-6) {
                return a;
            }
            double temp1 = sin((1 - fraction) * angle) / sinAngle;
            double temp2 = sin(fraction * angle) / sinAngle;
            // Converts from polar to vector and interpolate.
            double x = temp1 * cosFromLat * cos(fromLng) + temp2 * cosToLat * cos(toLng);
             double y = temp1 * cosFromLat * sin(fromLng) + temp2 * cosToLat * sin(toLng);
             double z = temp1 * sin(fromLat) + temp2 * sin(toLat);
            // Converts interpolated vector back to polar.
             double lat = atan2(z, sqrt(x * x + y * y));
            double lng = atan2(y, x);
            return new LatLng(toDegrees(lat), toDegrees(lng));     }
    }

}
