package com.example.mehar.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> extractNews(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log:Problem making HTTP request", e);
        }
        List<News> news = extractNewsFromJSON(jsonResponse);
        return news;
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Custom Log:Error with creating URL");
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractNewsFromJSON(String newsJSON) {
        ArrayList<News> news = new ArrayList<>();
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            int i;
            JSONObject root = new JSONObject(newsJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (i = 0; i < results.length(); i++) {
                JSONObject currentObject = results.getJSONObject(i);
                String publishDateAndTime = currentObject.getString("webPublicationDate");
                String sectionName = currentObject.getString("sectionName");
                String webTitle = currentObject.getString("webTitle");
                String webUrl = currentObject.getString("webUrl");
                JSONArray tags = currentObject.getJSONArray("tags");
                JSONObject obj;
                String contributor;
                try {
                    obj = tags.getJSONObject(0);
                    contributor = obj.getString("webTitle");
                } catch (JSONException e) {
                    contributor = "";
                }

                news.add(new News(sectionName, publishDateAndTime, webUrl, webTitle, contributor));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Custom Log:Problem parsing JSON results", e);
        }
        return news;
    }
}