package com.example.fisheralert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class location extends AppCompatActivity {

    /**
     * This class is for the layout "location.xml"
     *
     * This is a placeholder activity to display the location of the phone, to implement the distance
     * calculation. Later contributions may introduce new features
     */

    private TextView latTextView;
    private TextView lonTextView;
    private TextView dTextView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Coordinate port,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        Intent intent = getIntent();
        port = new Coordinate("Chennai",13.0815,80.2921);
        phone = new Coordinate();
        //button = findViewById(R.id.button3);

        latTextView = findViewById(R.id.lat);
        lonTextView = findViewById(R.id.lon);
        dTextView = findViewById(R.id.textView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /**
         * This is a Location Listener. It is a listener for detecting any changes in the location of the phone.
         */

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latTextView.setText("Lat: "+location.getLatitude());
                lonTextView.setText("Lon: "+ location.getLongitude());
                phone.setLatitude(location.getLatitude());
                phone.setLongitude(location.getLongitude());
                /**
                * This is an extremely inefficient calculation. It is a direct implementation of the formula to
                * calculate the distance between two geographic coordinates. I feel there is some library which
                * can do this in a fraction of a second.
                * */
                double a = Math.pow(Math.sin((phone.getLatitude()-port.getLatitude())/2),2) + Math.cos(phone.getLatitude())*Math.cos(port.getLatitude())*Math.pow(Math.sin((phone.getLongitude()-port.getLongitude())/2),2);
                double c = Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
                double d = c*6371000;
                dTextView.setText("Distance: "+d);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
        };


        /**
         * This is a LocationManager object which will request the location of the phone by GPS
         *
         */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
        }


    }

}

