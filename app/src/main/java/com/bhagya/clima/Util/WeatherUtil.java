package com.bhagya.clima.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bhagya.clima.R;

import java.util.Calendar;

public class WeatherUtil {
    public static String getDirection(int degree){
        if(degree == 0 || degree == 360){
            return "N";
        }else if(degree == 90){
            return  "E";
        }else if(degree == 180){
            return "S";
        }else if(degree == 270){
            return "W";
        }else if(degree > 0 && degree < 90){
            return "NE";
        }else if(degree > 90 && degree < 180){
            return "SE";
        }else if(degree > 180 && degree < 270){
            return "SW";
        }else{
            return "NW";
        }
    }

    public static String capitalize(@NonNull String sentence) {

        String[] words = sentence.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

    public static Drawable selectDrawable(@NonNull int status, Context context){

        if(status >= 200 && status <= 232){
            return context.getResources().getDrawable(R.drawable.thunderstorm, null);
        }else if(status >= 300 && status <= 321){
            if(isNight())
                return context.getResources().getDrawable(R.drawable.drizzle_night, null);
            else
                return context.getResources().getDrawable(R.drawable.drizzle, null);
        }else if(status >= 500 && status <= 531){
            if(isNight())
                return context.getResources().getDrawable(R.drawable.rain_night, null);
            else
                return context.getResources().getDrawable(R.drawable.rain, null);
        }else if(status >= 600 && status <= 622){
            return context.getResources().getDrawable(R.drawable.mist, null);
        }else if(status >= 701 && status <= 781){
            if(isNight())
                return context.getResources().getDrawable(R.drawable.fog_night, null);
            else
                return context.getResources().getDrawable(R.drawable.fog, null);
        }else if(status == 800){
            if(isNight())
                return context.getResources().getDrawable(R.drawable.clear_sky_night, null);
            else
                return context.getResources().getDrawable(R.drawable.clear_sky, null);
        }else if(status == 801){
            if(isNight())
                return context.getResources().getDrawable(R.drawable.few_clouds_night, null);
            else
                return context.getResources().getDrawable(R.drawable.few_clouds, null);
        }else if(status == 802){
            return context.getResources().getDrawable(R.drawable.scattered_clouds, null);
        }else if(status == 803 || status == 804){
            return context.getResources().getDrawable(R.drawable.broken_clouds, null);
        }else{
            if(isNight())
                return context.getResources().getDrawable(R.drawable.few_clouds_night, null);
            else
                return context.getResources().getDrawable(R.drawable.few_clouds, null);
        }

    }

    public static boolean isNight(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }

}
