package com.example.mehar.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String requestURL;

    public NewsLoader(Context context, String url) {
        super(context);
        requestURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<News> loadInBackground() {
        if (requestURL == null) {
            return null;
        }
        return QueryUtils.extractNews(requestURL);
    }
}