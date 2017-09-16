package com.example.android.quakereport;

/**
 * Created by wen-zhao on 9/9/2017.
 */

public class Earthquake {
    private double mMagnitude;
    private String lLocationCity;
    private Long mDate;
    private String url;
    public Earthquake(double mag,String city,Long date, String url){
        this.mMagnitude = mag;
        this.lLocationCity = city;
        this.mDate = date;
        this.url = url;

    }
    public double getmMagnitude (){
        return this.mMagnitude;
    }
    public String getlLocationCity(){
        return this.lLocationCity;
    }
    public Long getmDateMillesecondes(){
        return this.mDate;
    }
    public String getUrl(){
        return this.url;
    }


}
