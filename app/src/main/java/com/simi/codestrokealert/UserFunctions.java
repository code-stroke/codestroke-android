package com.simi.codestrokealert;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by user2 on 6/13/2016.-
    */
    public class UserFunctions
    {
        private JSONParser jsonParser;
        Context context;

    // constructor
    public UserFunctions(Context context)
    {
        this.context = context;
        jsonParser = new JSONParser();
    }

    public void msg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //  Login User
    public String LoginUser(String methodName, String jsonData) {
        // Building Parameters
        String urls = "";
        urls = methodName;

        Log.e("Login Urljson", urls);
        String json = jsonParser.postRequesthtpps(urls, jsonData);

        if (json.length() != 0) {
            json = json.trim();

            try {
                json = new JSONTokener(json).nextValue().toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return json;
    }
        //  Login User
        public String PasswordUser(String methodName, String jsonData) {
            // Building Parameters
            String urls = "";
            urls = methodName;

            Log.e("Login Urljson", urls);
            String json = jsonParser.postRequestAuth(urls, jsonData);

            if (json.length() != 0) {
                json = json.trim();

                try {
                    json = new JSONTokener(json).nextValue().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return json;
        }

        //  Login User
        public String ProfileUser(String methodName, String jsonData) {
            // Building Parameters
            String urls = "";
            urls = methodName;

            Log.e("Login Urljson", urls);
            String json = jsonParser.getRequestAuth(urls, jsonData);

            if (json.length() != 0) {
                json = json.trim();

                try {
                    json = new JSONTokener(json).nextValue().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return json;
        }
    /*public String multipost
        (String methodName, MultipartEntity entry) {
        // Building Parameters
        String urls = "";
        urls = methodName;

        Log.e("Login Urljson", urls);
        String json = jsonParser.postRequesthtpps_multy(urls, entry);
        //	Log.e("new res", "res"+json);
        //String jsonFormattedString = json.replaceAll("\\\\", "");
        if (json.length() != 0) {
            json = json.trim();
          *//*  json = json.substring(1, json.length() - 1);
            json = json.replace("\\", "");*//*
            try {
                json = new JSONTokener(json).nextValue().toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Log.e("new res", json);
        }
        return json;
    }*/

}
