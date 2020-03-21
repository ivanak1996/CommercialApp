package com.example.commercialapp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.*;

import com.example.commercialapp.roomDatabase.deliveryPlaces.DeliveryPlace;
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

    public static LoginUserResult getUserDataFromApi(String username, String pass) {

        ContentValues nameValuePairs = new ContentValues();

        nameValuePairs.put("userEmail", username);
        nameValuePairs.put("userPass", pass);
        nameValuePairs.put("action", "login");

        JSONObject result = getJSONFromUrl(API_URL, nameValuePairs);

        if (result != null) {
            try {
                JSONArray list = result.getJSONArray("login");

                JSONObject o = list.getJSONObject(0);
                User u = getUserFromJSONObject(o);

                // add delivery places as well
                JSONArray deliveryPlaces = o.getJSONArray("MESTOISPORUKE");
                ArrayList<DeliveryPlace> deliveryPlacesList = new ArrayList<>();

                for (int i = 0; i < deliveryPlaces.length(); i++) {
                    JSONObject dp = deliveryPlaces.getJSONObject(i);
                    deliveryPlacesList.add(getDeliveryPlaceFromJSONObject(dp, u.getRowId()));
                }

                return new LoginUserResult(u, deliveryPlacesList);

            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return null;
    }

    private static DeliveryPlace getDeliveryPlaceFromJSONObject(JSONObject o, int userRowId) throws JSONException {
        String acName2 = o.getString("acName2");
        String acAddress = o.getString("acAddress");
        String acCity = o.getString("acCity");
        String anQId = o.getString("anQId");
        String acPost = o.getString("acPost");
        String acSubject = o.getString("acSubject");

        return new DeliveryPlace(userRowId, acName2, acAddress, acCity, anQId, acPost, acSubject);
    }

    private static User getUserFromJSONObject(JSONObject o) throws JSONException {
        String acName2 = o.getString("acName2");
        String acAddress = o.getString("acAddress");
        String acRegNo = o.getString("acRegNo");
        String acSubject = o.getString("acSubject");
        String password = o.getString("password");
        String anDoeplo = o.getString("anDoeplo");
        String acCity = o.getString("acCity");
        String anNedospelo = o.getString("anNedospelo");
        String acCode = o.getString("acCode");
        String id = o.getString("id");
        String acClerk = o.getString("acClerk");
        String acPost = o.getString("acPost");
        String email = o.getString("email");
        String anLimit = o.getString("anLimit");
        return new User(acName2, acAddress, acRegNo, acSubject, password, anDoeplo, acCity, anNedospelo, acCode, id, acClerk, acPost, email, anLimit);
    }
}
