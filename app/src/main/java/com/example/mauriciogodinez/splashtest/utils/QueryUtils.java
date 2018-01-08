package com.example.mauriciogodinez.splashtest.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<PagaTodo> fetchPagaTodoData(String requestUrl) {
        // Create URL object
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("OS","Android");
            urlConnection.setRequestProperty("Version","2.5.2");
            urlConnection.connect();

//Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("pass", "password");
            jsonParam.put("user", "Luis");

            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream ());
            printout.writeBytes(URLEncoder.encode(jsonParam.toString(),"UTF-8"));
            printout.flush();
            printout.close();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the PagaTodo JSON results.", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    /**
     * Return a list of {@link PagaTodo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<PagaTodo> extractFeatureFromJson(String PagaTodoJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(PagaTodoJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding PagaTodos to
        List<PagaTodo> PagaTodos = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(PagaTodoJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or PagaTodos).

            // For each PagaTodo in the PagaTodoArray, create an {@link PagaTodo} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                // Get a single PagaTodo at position i within the list of PagaTodos
                // Extract the value for the key called "mag"
                String agente = baseJsonResponse.getString("agente");
                // Extract the value for the key called "place"
                String error = baseJsonResponse.getString("error");
                // Extract the value for the key called "time"
                int id_user = baseJsonResponse.getInt("id_user");
                // Extract the value for the key called "url"
                String token = baseJsonResponse.getString("token");

                PagaTodo PagaTodo = new PagaTodo(agente, error, id_user, token);
                // Add the new {@link PagaTodo} to the list of PagaTodos.
                PagaTodos.add(PagaTodo);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the PagaTodo JSON results", e);
        }

        // Return the list of PagaTodos
        return PagaTodos;
    }
}
