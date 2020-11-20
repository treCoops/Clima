package com.bhagya.clima.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bhagya.clima.Adapter.WeatherAdapter;
import com.bhagya.clima.Helper.AlertBar;
import com.bhagya.clima.Helper.ProgressDialog;
import com.bhagya.clima.Helper.Validator;
import com.bhagya.clima.Model.WeatherModel;
import com.bhagya.clima.R;
import com.bhagya.clima.Util.ConnectionUtil;
import com.bhagya.clima.Util.WeatherUtil;
import com.bhagya.clima.Util.LocationUtil;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {

    TextView txtTemperatureMain, txtWeatherDescription, txtUsername, txtCity, txtHumidity, txtPressure, txtWind;
    ImageView weatherIcon;
    String cityName;
    ProgressDialog progressDialog;
    Vibrator vibrator;

    RecyclerView recDays;
    RecyclerView.LayoutManager layoutManager;
    WeatherAdapter weatherAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConnectionUtil.isInternetAvailable(getApplicationContext(), HomeActivity.this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Button btnLogout = findViewById(R.id.btnLogout);
        ImageView imgSearch = findViewById(R.id.imgSearch);
        ImageView imgLocate = findViewById(R.id.imgLocate);
        EditText txtSearchCity = findViewById(R.id.txtSearchCity);
        txtTemperatureMain = findViewById(R.id.txtTemperatureMain);
        txtWeatherDescription = findViewById(R.id.txtWeatherDescription);
        txtUsername = findViewById(R.id.txtUsername);
        txtCity = findViewById(R.id.txtCity);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtPressure = findViewById(R.id.txtPressure);
        txtWind = findViewById(R.id.txtWind);
        weatherIcon = findViewById(R.id.weatherIcon);
        recDays = findViewById(R.id.recDays);
        ConnectionUtil.isInternetAvailable(getApplicationContext(), HomeActivity.this);

        progressDialog = new ProgressDialog();

        txtUsername.setText("Howdy, "+LoginActivity.user.getFullName());

        imgSearch.setOnClickListener(v -> {

            if(!ConnectionUtil.isInternetAvailable(getApplicationContext(), HomeActivity.this)){
                return;
            }

            if(!Validator.validatePersonName(txtSearchCity.getText().toString().trim())){
                AlertBar.notifyWarning(HomeActivity.this, "Enter a valid city name.");
                vibrator.vibrate(5);
                return;
            }

            getMainWeatherData(txtSearchCity.getText().toString().trim());
            txtSearchCity.setText("");
        });

        getLocation();
        getMainWeatherData(cityName);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            Animatoo.animateSlideRight(HomeActivity.this);
            finishAffinity();
        });

        imgLocate.setOnClickListener(v -> {
            getLocation();
            getMainWeatherData(cityName);
        });

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recDays.setLayoutManager(layoutManager);
    }

    void initRecycleView(List<WeatherModel> weatherData){
        weatherAdapter = new WeatherAdapter(weatherData, getApplicationContext());
        recDays.setAdapter(weatherAdapter);


    }

    void getLocation() {

        LocationUtil locationUtil = new LocationUtil(getApplicationContext());

        if (locationUtil.isGPSEnabled && locationUtil.isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationUtil.getLocation(getApplicationContext());

            try{
                List<Address> address =  locationUtil.getCity(getApplicationContext(), location.getLatitude(), location.getLongitude());
                txtCity.setText(address.get(0).getLocality()+", "+address.get(0).getCountryName());
                cityName = address.get(0).getLocality();
            }catch (NullPointerException ex){
                Log.e("NUll", ex.getMessage());
                getLocation();
            }



        }else{
            startActivity(new Intent(HomeActivity.this, LocationActivity.class));
            Animatoo.animateSlideLeft(HomeActivity.this);
        }

    }

    void populateDayWeather(Double lat, Double lng){
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+String.valueOf(lat)+"&lon="+String.valueOf(lng)+"&exclude=current&appid=67984f9621e81d1d6439ade3f20c043b&units=metric";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismissProgressBar();
                    JSONArray daily = response.getJSONArray("daily");
                    List<WeatherModel> data = new ArrayList<>();

                    for(int a=1; a < daily.length(); a++){
                        JSONObject day = daily.getJSONObject(a);
                        JSONObject temp = day.getJSONObject("temp");
                        JSONArray jsonWeatherArray = day.getJSONArray("weather");
                        JSONObject jsonObject = jsonWeatherArray.getJSONObject(0);


                        TimeZone timeZone = TimeZone.getTimeZone(response.getString("timezone"));
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        sdf.setTimeZone(timeZone);
                        String localTime = sdf.format(new Date(day.getLong("dt") * 1000));
                        data.add(new WeatherModel(localTime, "1234", temp.getString("day"), jsonObject.getInt("id")));
                    }

                    initRecycleView(data);


                }catch (JSONException ex){
                    Log.e("Json Error", ex.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismissProgressBar();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.statusCode == 404) {
                    AlertBar.notifyDanger(HomeActivity.this, "Unable to load day forecast!");
                    vibrator.vibrate(5);
                }
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    void getMainWeatherData(String cityName){

        progressDialog.showProgressBar(HomeActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=67984f9621e81d1d6439ade3f20c043b&units=metric";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.getInt("cod") == 200){
                        updateUI(response);
                    }else if(response.getInt("cod") == 404){
                        progressDialog.dismissProgressBar();
                        AlertBar.notifyDanger(HomeActivity.this, "Enter a valid city, Try again!");
                        vibrator.vibrate(5);
                    }

                }catch (JSONException ex){
                    Log.e("Json Error", ex.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismissProgressBar();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.statusCode == 404) {
                    AlertBar.notifyDanger(HomeActivity.this, "Enter a valid city, Try again!");
                    vibrator.vibrate(5);
                }
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    void updateUI(JSONObject object){
        try {
            JSONObject jsonMainObject = object.getJSONObject("main");
            txtHumidity.setText(jsonMainObject.getString("humidity")+"%");
            txtPressure.setText(jsonMainObject.getString("pressure")+"hPa");
            txtTemperatureMain.setText(jsonMainObject.getString("temp")+"\u00B0C");

            JSONObject jsonWindObject = object.getJSONObject("wind");
            txtWind.setText(WeatherUtil.getDirection(jsonWindObject.getInt("deg"))+" "+String.format("%.1f",jsonWindObject.getDouble("speed")*3.6)+" kmh");

            JSONObject jsonCoordinateObject = object.getJSONObject("coord");

            Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> address = geoCoder.getFromLocation(jsonCoordinateObject.getDouble("lat"), jsonCoordinateObject.getDouble("lon"), 1);
            txtCity.setText(object.getString("name")+", "+address.get(0).getCountryName());

            populateDayWeather(jsonCoordinateObject.getDouble("lat"), jsonCoordinateObject.getDouble("lon"));

            JSONArray jsonWeatherArray = object.getJSONArray("weather");
            JSONObject jsonObject = jsonWeatherArray.getJSONObject(0);
            txtWeatherDescription.setText(WeatherUtil.capitalize(jsonObject.getString("description")));
            weatherIcon.setImageDrawable(WeatherUtil.selectDrawable(jsonObject.getInt("id"), getApplicationContext()));

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }






}