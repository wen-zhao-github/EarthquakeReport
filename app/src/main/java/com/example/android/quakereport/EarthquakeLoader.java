package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by wen-zhao on 9/11/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    String url;
    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }
    //load in background ,take an url as parameter and return back a list parsed through JSON
    @Override
    public List<Earthquake> loadInBackground() {
        if (this.url != null){
            List<Earthquake> result = QueryUtils.fetchEarthquakeData(url);
            return result;

        }else{
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
