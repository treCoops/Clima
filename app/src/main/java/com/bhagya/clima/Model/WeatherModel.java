package com.bhagya.clima.Model;

import android.graphics.drawable.Drawable;

public class WeatherModel {

    String day;
    String date;
    String temp;
    int status;

    public WeatherModel(){}

    public WeatherModel(String day, String date, String temp, int status){
        this.day = day;
        this.date = date;
        this.temp = temp;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public int getImg() {
        return status;
    }
}
