package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Om on 20-Apr-17.
 */

public class BookLoader extends AsyncTaskLoader<List<bookListing>> {
    private static final String LOG_TAG = BookLoader.class.getName();

    private String url;

    public BookLoader(Context context, String ur) {
        super(context);
        url = ur;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "Loader started loading");
    }

    @Override
    public List<bookListing> loadInBackground() {
        if (url == null) {
            return null;
        }


        List<bookListing> result = QueryUtils.fetchBookData(url);
        Log.i(LOG_TAG, "Loader is loading in background");
        return result;
    }
}
