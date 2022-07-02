package com.example.newsapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<ANews>> {
    /** Query URL */
    private final String bUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        bUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ANews> loadInBackground() {
        if (bUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<ANews> books = QueryUtils.fetchNewsData(bUrl);
        return books;
    }
}

