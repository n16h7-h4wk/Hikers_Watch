package com.developer.ankit.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.*;
import java.util.*;
import java.lang.Exception;

public class MainActivity extends AppCompatActivity {
    LocationManager mLocationManager ;
    LocationListener mLocationListener ;
    TextView mLatTextView , mLonTextView, mAccTextView, mAltTextView, mAddTextView ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
            }
        }
    }

    public void updateLocation(Location location) throws Excepion  {
        mLatTextView.setText("Latitude : " + Double.toString(Math.rint(location.getLatitude())));
        mLonTextView.setText("Longitude : " + Double.toString(Math.rint(location.getLongitude())));
        mAccTextView.setText("Accuracy : " + Double.toString(location.getAccuracy()));
        mAltTextView.setText("Altitude : " + Double.toString(location.getAltitude()));
        Geocoder mGeoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = " Couldn\'t find Address!";
        List<Address> listAddresses = null;
        
            listAddresses = mGeoCoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(listAddresses!=null && listAddresses.size()>0){
                address = "Address : ";
                if(listAddresses.get(0).getSubThoroughfare()!=null){
                    address+=listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if(listAddresses.get(0).getThoroughfare()!=null){
                    address+=listAddresses.get(0).getThoroughfare() + "\n";
                }
                if(listAddresses.get(0).getLocality()!=null){
                    address+=listAddresses.get(0).getLocality() + "\n";
                }
                if(listAddresses.get(0).getPostalCode()!=null){
                    address+=listAddresses.get(0).getPostalCode() + "\n";
                }
                if(listAddresses.get(0).getCountryName()!=null){
                    address+=listAddresses.get(0).getCountryName();
                }
            }
            mAddTextView.setText(address);
       


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLatTextView = (TextView) findViewById(R.id.lat_text_view);
        mLonTextView = (TextView) findViewById(R.id.lon_text_view);
        mAccTextView = (TextView) findViewById(R.id.acc_text_view);
        mAltTextView = (TextView) findViewById(R.id.alt_text_view);
        mAddTextView = (TextView) findViewById(R.id.add_text_view);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);

            Location mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(mLocation!=null){
                updateLocation(mLocation);
            }
        }


    }
}
