package com.example.newsapplication;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    //private static final String LOG_TAG =  QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static List<ANews> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            assert url != null;
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("ProblemWithHTTP", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s

        // Return the list of {@link Earthquake}s
        return extractNews(jsonResponse);
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("ProblemWithURL", "Error with creating URL", exception);
            return null;
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int responseStatusCode = urlConnection.getResponseCode();
            if( responseStatusCode != HttpURLConnection.HTTP_OK ) {
                inputStream = urlConnection.getErrorStream();
                Log.e("Error is", String.valueOf(responseStatusCode));
                //Get more information about the problem
            } else {
                inputStream = urlConnection.getInputStream();
            }
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e("ProblemWithConnect", "Error with connecting", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    /**
     * Return a list of {@link ANews} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<ANews> extractNews(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<ANews> newsArrayList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);
            JSONArray results = baseJSONResponse.getJSONArray("results");

            for(int i = 0; i < results.length(); i++){
                JSONObject allObjects = results.getJSONObject(i);
                String title = allObjects.getString("title");
                String time = allObjects.getString("pubDate");
                String imageURL = allObjects.getString("image_url");
                String sourceName = allObjects.getString("source_id");

                ANews news = new ANews(title,time,sourceName,imageURL);
                newsArrayList.add(news);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of earthquakes
        return newsArrayList;
    }

}
