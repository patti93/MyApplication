package com.example.patrick.myapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class VolleySingleton {

    private static VolleySingleton instance;
    private static RequestQueue queue;



    private VolleySingleton(){
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(VolleySingleton.instance == null){
            VolleySingleton.instance = new VolleySingleton();
            queue = Volley.newRequestQueue(context);
        }
        return VolleySingleton.instance;
    }
    public static void addToRequestQueue(StringRequest request){
        queue.add(request);
    }

}
