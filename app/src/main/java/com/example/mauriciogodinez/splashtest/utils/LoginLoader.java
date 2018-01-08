package com.example.mauriciogodinez.splashtest.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/*
 * Created by mauriciogodinez on 19/05/17.
 */

public class LoginLoader extends AsyncTaskLoader<List<PagaTodo>> {

    /** Tag for log messages */
    private static final String LOG_TAG = LoginLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link LoginLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public LoginLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<PagaTodo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of PagaTodos.
        return QueryUtils.fetchPagaTodoData(mUrl);
    }
}
