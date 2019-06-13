package com.example.patrick.myapplication;

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(String response);
}