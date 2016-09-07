package com.example.android.quakereport.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.model.Earthquake;
import com.example.android.quakereport.Utils;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String url;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null) {
            return null;
        }
        return Utils.fetchEarthquakeData(url);
    }
}
