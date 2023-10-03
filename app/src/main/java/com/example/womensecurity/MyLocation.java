package com.example.womensecurity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensecurity.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation extends AppCompatActivity  implements LocationListener {

    TextView myLoc;

    AppCompatImageView back_to_home;
    LinearLayout messLay;
    LocationManager locationManager;
    String country , link , locality , city , state,subArea;
    double lontitude ,latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        getSupportActionBar().hide();

        myLoc = findViewById(R.id.myLoc);
        back_to_home = findViewById(R.id.back_to_home);
        messLay = findViewById(R.id.messLay);

        if (ContextCompat.checkSelfPermission(MyLocation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyLocation.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        else
        {
            getLocation();
        }

        myLoc.setText(link);
        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLocation.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(messLay, "Denied", Snackbar.LENGTH_SHORT).show();

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MyLocation.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(MyLocation.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            subArea = addresses.get(0).getSubAdminArea();
            latitude = addresses.get(0).getLatitude();
            lontitude = addresses.get(0).getLongitude();
            locality = addresses.get(0).getAddressLine(0);
            link  = "Country: " + country + "\nCity: " + city + "\nArea: " + locality + "\nSubArea: " + subArea + "\nLatitude: " + latitude
                    + "\nLongitude: " + lontitude;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {

        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Snackbar.make(messLay,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}