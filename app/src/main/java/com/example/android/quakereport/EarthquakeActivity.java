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

import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.quakereport.loader.EarthquakeLoader;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String BASE_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=15";
    private EarthquakeAdapter earthquakeAdapter;
    private ListView earthquakeListView;
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        earthquakeListView = (ListView) findViewById(R.id.list_earthquakes);

        // Create a new {@link ArrayAdapter} of earthquakes
        earthquakeAdapter = new EarthquakeAdapter(getApplicationContext(), new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this, BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, final List<Earthquake> data) {

        // Clear the adapter of previous earthquake data
        earthquakeAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            earthquakeAdapter.addAll(data);
        }
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake = data.get(position);
                Uri uri = Uri.parse(earthquake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Clear the adapter of previous earthquake data
        earthquakeAdapter.clear();
    }
}
