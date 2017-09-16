package com.example.android.quakereport;

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

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from an url.
 */
public final class QueryUtils {
    //util class
    private QueryUtils() {
    }
    /**
     * Query the USGS dataset and return an {@link Earthquake} object to represent a single earthquake.
     */
    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            // Perform HTTP request to the URL and receive a JSON response back
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the {@link Event}
        return earthquakes;
    }
    /**
     * Returns new URL object from the given string URL.
     * @param stringUrl as a string
     * @return URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * @param url created with a string url
     * @return JSON response as string
     */
    private static String makeHttpRequest(URL url) throws IOException {
        //declare a empty JSON response string
        String jsonResponse = "";
        // If the URL is null, then return empty JSON response
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //initialize a HttpURLConnection on the url object
            urlConnection = (HttpURLConnection) url.openConnection();
            //set HttpURLConnection parameters
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            //start the connection
            urlConnection.connect();

            // If the request was successful (response code 200) then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            //close both HttpURLConnection and the file stream
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the server.
     * @param inputStream
     * @return JSON response as string
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            //InputStreamReader read one character at a time
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            //wrap inefficient InputStreamReader in a BufferedReader
            BufferedReader reader = new BufferedReader(inputStreamReader);
            //read response line by line
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Earthquake} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     * @param  jsonResponse JSON response as string
     * @return  Earthquake objects stored in an ArrayList
     */
        private static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        // Try to parse JSON response If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");
            for (int i = 0; i< featuresArray.length(); i++){
                JSONObject currentFeature = featuresArray.getJSONObject(i);
                JSONObject currentObject = currentFeature.getJSONObject("properties");
                Double magnitude = currentObject.getDouble("mag");
                String city = currentObject.getString("place");
                Long date = currentObject.getLong("time");
                String webUrl = currentObject.getString("url");
                earthquakes.add(new Earthquake(magnitude,city,date,webUrl));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }

}