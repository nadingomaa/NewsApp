package com.example.connect.newsapp;

import android.net.Uri;
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

import static android.content.ContentValues.TAG;

public class QueryUtils {

    private QueryUtils() {
    }

    private static List<News> extractFeaturesFromFeatures(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);
                String webTitle = currentNews.getString("webTitle");
                String sectionName = currentNews.getString("sectionName");
                String webPublicationDate = currentNews.getString("webPublicationDate");
                String webUrl = currentNews.getString("webUrl");

                News New = new News(webTitle, sectionName, webPublicationDate, webUrl);
                news.add(New);
                Log.i(TAG, "extractFeaturesFromFeatures: " + news.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "extractFeaturesFromFeatures: " + e);
        }
        return news;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl.toString());
            Log.i(TAG, "createUrl: " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "createUrl: " + e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStrem(inputStream);
                Log.i(TAG, "makeHttpRequest: " + jsonResponse);
            } else {
                Log.i(TAG, "error response: " + httpURLConnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.i(TAG, "problem with retrive json results" + e);

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStrem(InputStream inputStream) throws IOException {
        StringBuilder out = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while ((line != null)) {
                out.append(line);
                line = reader.readLine();
            }
        }
        return out.toString();
    }

    public static List<News> featchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.i(TAG, "featchNewData: " + e);
        }
        List<News> news = extractFeaturesFromFeatures(jsonResponse);
        return news;
    }
}
