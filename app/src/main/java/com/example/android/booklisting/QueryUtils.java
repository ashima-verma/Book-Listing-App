package com.example.android.booklisting;

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

/**
 * Created by Om on 20-Apr-17.
 */

public class QueryUtils {

    private QueryUtils() {

    }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<bookListing> fetchBookData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Error in thread", e);
        }
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in input stream", e);
        }
        List<bookListing> bookListings = extractFeatureFromJson(jsonResponse);
        Log.i(LOG_TAG, "Fetching BookList Data");
        return bookListings;
    }

    private static URL createUrl(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in creating url");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.i(LOG_TAG, "makeHttpRequest: " + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book list JSON results.", e);

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

    private static List<bookListing> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<bookListing> bookListings = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            Log.d("query", baseJsonResponse.toString());
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.optJSONObject("volumeInfo");
                String title = volumeInfo.optString("title");
                String authors = volumeInfo.optString("authors");
                String publisher = volumeInfo.optString("publisher");

                bookListings.add(new bookListing(title, authors, publisher));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils.java", "Problem parsing the book list JSON results", e);
        }
        return bookListings;

    }
}
