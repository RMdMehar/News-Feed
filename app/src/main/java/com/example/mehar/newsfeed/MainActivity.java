package com.example.mehar.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String requestURL = "http://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    LoaderManager loaderManager;
    private NewsAdapter adapter;
    private TextView emptyState;
    private String queryParam = "";
    private String oldQueryParam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = findViewById(R.id.list);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);
        emptyState = findViewById(R.id.empty_state_text);
        newsListView.setEmptyView(emptyState);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = adapter.getItem(position);
                String url = currentNews.getWebURL();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork != null) && (activeNetwork.isConnected())) {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyState.setText(R.string.search_hint);
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyState.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(requestURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", queryParam);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        emptyState.setText(R.string.empty);
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.VISIBLE);
        adapter.clear();
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        adapter.clear();
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        } else {
            emptyState.setText(R.string.not_found);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        MenuItem item = menu.findItem(R.id.search_query);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryParam = query;
                loaderManager = getLoaderManager();
                if (oldQueryParam.equals("")) {
                    loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    oldQueryParam = queryParam;
                } else {
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                }
                searchView.clearFocus();
                setProgressBarIndeterminateVisibility(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}