package com.example.patrick.myapplication;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class VolleyHelper {


    public static void makeStringRequestPOST(Context context, String url, final Map<String,String> params, final VolleyResponseListener listener) {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }
        };
        //RequestQueue queue =  Volley.newRequestQueue(context);

        //queue.add(stringRequest);
        // Access the RequestQueue through singleton class.
         VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void makeStringRequestGET(Context context, String url,final VolleyResponseListener listener ){

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}
