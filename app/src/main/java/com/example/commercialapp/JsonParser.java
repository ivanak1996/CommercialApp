package com.example.commercialapp;

import java.io.*;
import java.net.*;
import org.json.*;
import com.example.commercialapp.roomDatabase.user.User;
import android.content.ContentValues;
import android.util.Log;


public class JsonParser {

    final static String TAG = "JsonParser.java";
    final static String API_URL = "http://89.216.122.162:8080/tkomserver/jsonData/api";

    static JSONObject jObj = null;
    static String json = "";

    public static String getJSONFromUrlStr(String url, ContentValues nameValuePairs) {
        try {
            String charset = "UTF-8";
            String userEmail = nameValuePairs.getAsString("userEmail");
            String userPass = nameValuePairs.getAsString("userPass");
            String action = nameValuePairs.getAsString("action");
            String query = String.format("userEmail=%s&userPass=%s&action=%s",
                    URLEncoder.encode(userEmail, charset),
                    URLEncoder.encode(userPass, charset),
                    URLEncoder.encode(action, charset));

            Log.d("url", query);

            String urlQuery = url;

            URLConnection connection = new URL(urlQuery).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes(charset));

            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 1024 * 100);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONFromUrl(String url, ContentValues nameValuePairs) {

        json = getJSONFromUrlStr(url, nameValuePairs);
        Log.e(TAG, "Error String" + json);
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public static User getUserFromApi(String username, String password) {

        ContentValues nameValuePairs = new ContentValues();

        nameValuePairs.put("userEmail", username);
        nameValuePairs.put("userPass", password);
        nameValuePairs.put("action", "login");

        JSONObject result = getJSONFromUrl(API_URL, nameValuePairs);

        if (result != null) {
            try {
                JSONArray list = result.getJSONArray("login");

                JSONObject o = list.getJSONObject(0);
                String e = o.getString("email");
                String pass = o.getString("password");
                String reg = o.getString("acRegNo");
                User u = new User(e, pass, reg, API_URL);
                return u;
            } catch (Exception e) {
                //TODO: handle exception
            }
        }

        return null;
    }
}
