package Util;


import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by piranavan on 2017-02-08.
 */

public class Utility {
    public static final String API_ = "a17466876960d0fb43a76f7a9350cdc0";
    public static final String BASE_ = "http://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String lat, String lon){
        StringBuilder sb = new StringBuilder(BASE_);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric",lat,lon,API_));
        return sb.toString();
    }

    @NonNull
    public static String apiRequest(String id){
        StringBuilder sb = new StringBuilder(BASE_);
        sb.append(String.format("?id=%s&units=metric",id,API_));
        return sb.toString();
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    public static String getImage(String icon){
        return String.format("http://openweathermap.org/img/w/%s.png",icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
