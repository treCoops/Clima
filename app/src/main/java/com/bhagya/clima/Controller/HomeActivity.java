package com.bhagya.clima.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhagya.clima.R;
import com.bhagya.clima.Util.ConnectionUtil;
import com.bhagya.clima.Util.LocationUtil;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    TextView txtTemperatureMain, txtWeatherDescription, txtUsername, txtCity, txtHumidity, txtPressure, txtWind;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConnectionUtil.isInternetAvailable(getApplicationContext(), HomeActivity.this);

        Button btnLogout = findViewById(R.id.btnLogout);
        txtTemperatureMain = findViewById(R.id.txtTemperatureMain);
        txtWeatherDescription = findViewById(R.id.txtWeatherDescription);
        txtUsername = findViewById(R.id.txtUsername);
        txtCity = findViewById(R.id.txtCity);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtPressure = findViewById(R.id.txtPressure);
        txtWind = findViewById(R.id.txtWind);

        weatherIcon = findViewById(R.id.weatherIcon);

        txtUsername.setText("Howdy, "+LoginActivity.user.getFullName());

        getLocation();

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            Animatoo.animateSlideRight(HomeActivity.this);
            finishAffinity();
        });

    }

    void getLocation() {

        LocationUtil locationUtil = new LocationUtil(getApplicationContext());

        if (locationUtil.isGPSEnabled && locationUtil.isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationUtil.getLocation(getApplicationContext());
            List<Address> address =  locationUtil.getCity(getApplicationContext(), location.getLatitude(), location.getLongitude());

            txtCity.setText(address.get(0).getLocality()+", "+address.get(0).getCountryName());

        }else{
            startActivity(new Intent(HomeActivity.this, LocationActivity.class));
            Animatoo.animateSlideLeft(HomeActivity.this);
        }

    }
}