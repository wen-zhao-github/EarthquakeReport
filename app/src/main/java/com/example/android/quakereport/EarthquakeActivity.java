/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Earthquake>>{
    EarthquakeAdapter adapter;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static String urlRequest = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=15";

    //override LoaderCallBacks methods
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        EarthquakeLoader loader = new EarthquakeLoader(this,urlRequest);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        adapter.clear();
        if(earthquakes != null && !earthquakes.isEmpty()){
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader){
            adapter.addAll(new ArrayList<Earthquake>());
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
         // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter( this, new ArrayList<Earthquake>());
        // Set the adapter on the {@link ListView} so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        //initiate the loader
        getLoaderManager().initLoader(1, null,this);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake earthquakeClicked = (Earthquake) earthquakeListView.getItemAtPosition(i);
                String earthquakeUrl = earthquakeClicked.getUrl();
                //send out a implicit intent with a link to be opened
                Intent openUrlInent = new Intent(Intent.ACTION_VIEW);
                openUrlInent.setData(Uri.parse(earthquakeUrl));
                startActivity(openUrlInent);
            }
        });
    }

}
