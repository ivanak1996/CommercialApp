package com.example.commercialapp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import com.example.commercialapp.models.LoginUserResult;
import com.example.commercialapp.models.ProductGroupModel;
import com.example.commercialapp.roomDatabase.deliveryPlaces.DeliveryPlace;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.user.User;

import android.util.Log;

// TODO: think of a more intelligent way to generate query string
public class JsonParser {

    final static String TAG = "JsonParser.java";
    final static String API_URL = "http://89.216.122.162:8080/tkomserver/jsonData/api";
    public final static int ID_PRODUCT_NOT_SAVED = -1;
    static JSONObject jObj = null;
    static String json = "";

    public static String getJSONFromUrlStrQuery(String url, String q) {
        try {
            String charset = "UTF-8";
            String query = q;

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

    public static JSONObject getJSONFromUrlQuery(String url, String q) {

        json = getJSONFromUrlStrQuery(url, q);
        Log.e(TAG, "Error String" + json);
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public static List<ProductGroupModel> getProductGroupsFromApi(String userEmail, String userPassword) {
        ArrayList<ProductGroupModel> productGroups = new ArrayList<>();

        String charset = "UTF-8";
        String action = "groups";
        String query;

        try {
            query = String.format("userEmail=%s&userPass=%s&action=%s",
                    URLEncoder.encode(userEmail, charset),
                    URLEncoder.encode(userPassword, charset),
                    URLEncoder.encode(action, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return productGroups;
        }

        JSONObject result = getJSONFromUrlQuery(API_URL, query);

        if (result != null) {
            try {
                JSONArray list = result.getJSONArray("groups");
                for (int j = 0; j < list.length(); j++) {
                    JSONObject o = list.getJSONObject(j);
                    String a = o.getString("a").trim();
                    String b = o.getString("b");
                    productGroups.add(new ProductGroupModel(a, b));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productGroups;

    }

    public static List<Product> getProductsFromApi(String userEmail, String userPassword, String keyword) {

        ArrayList<Product> productModels = new ArrayList<>();

        String charset = "UTF-8";
        String action = "products";
        String query;

        try {
            query = String.format("userEmail=%s&userPass=%s&action=%s&data=%%25%s%%25",
                    URLEncoder.encode(userEmail, charset),
                    URLEncoder.encode(userPassword, charset),
                    URLEncoder.encode(action, charset),
                    URLEncoder.encode(keyword, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return productModels;
        }

        JSONObject result = getJSONFromUrlQuery(API_URL, query);

        if (result != null) {
            try {
                JSONArray list = result.getJSONArray("products");
                for (int j = 0; j < list.length(); j++) {
                    JSONObject o = list.getJSONObject(j);
                    String a = o.getString("a");
                    String b = o.getString("b");
                    String c = o.getString("c");
                    String d = o.getString("d");
                    String e = o.getString("e");
                    String f = o.getString("f");
                    String v = o.getString("v");
                    String p = o.getString("p");
                    String r = o.getString("r");
                    String i = o.getString("i");
                    // TODO: edit this
                    productModels.add(new Product(ID_PRODUCT_NOT_SAVED, 0, a, b, c, d, e, f, v, p, r, i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productModels;
    }

    public static LoginUserResult getUserDataFromApi(String userEmail, String userPassword) {

        String charset = "UTF-8";
        String action = "login";
        String query;

        try {
            query = String.format("userEmail=%s&userPass=%s&action=%s",
                    URLEncoder.encode(userEmail, charset),
                    URLEncoder.encode(userPassword, charset),
                    URLEncoder.encode(action, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject result = getJSONFromUrlQuery(API_URL, query);

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


    private static DeliveryPlace getDeliveryPlaceFromJSONObject(JSONObject o, long userRowId) throws JSONException {
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
