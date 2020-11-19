package com.bhagya.clima.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.bhagya.clima.R;
import com.bhagya.clima.Util.LocationUtil;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class LocationActivity extends AppCompatActivity {

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LottieAnimationView lottie = findViewById(R.id.animation_view);
        Button btnEnable = findViewById(R.id.btnEnable);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        lottie.setAnimation("location.json");
        lottie.enableMergePathsForKitKatAndAbove(true);
        lottie.loop(true);
        lottie.playAnimation();

        btnEnable.setOnClickListener(v -> {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocationUtil locationUtil = new LocationUtil(getApplicationContext());

        if(flag != 0){
            if (locationUtil.isGPSEnabled && locationUtil.isNetworkEnabled) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LocationActivity.this, HomeActivity.class));
                        Animatoo.animateSlideRight(LocationActivity.this);
                        finishAffinity();
                    }
                },2500);
            }
        }

        flag++;
    }
}