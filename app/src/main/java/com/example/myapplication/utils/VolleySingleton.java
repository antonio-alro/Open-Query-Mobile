package com.example.myapplication.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ANTONIO on 09/01/2016.
 */
public class VolleySingleton {

    /**
     * ATTRIBUTES of VolleySingleton Class
     */
    private static VolleySingleton mInstance;   // Instance of Singleton Class
    private RequestQueue mRequestQueue;         // Request Queue
    private static Context mContext;            // Context


    /**
     * CONSTRUCTOR for VolleySingleton Class
     * @param context   context of the application
     */
    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }


    /**
     * METHOD to get the instance of VolleySingleton Class
     * @param context   context of the application
     * @return          a instance of the singleton class
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * METHOD to get the request queue of the class
     * @return  the queue with all the requests
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * METHOD to add a request to the request queue of the class
     * @param req   request HTTP
     * @param <T>   generic type
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

/*
// PETICION CON LIBRERIA VOLLEY de tipo StringRequest

String url ="https://www.google.es/?gws_rd=ssl";

// Request a string response from the provided URL.
StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    mTextView.setText("Response is: " + response.substring(0, 500));
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                    mTextView.setText("That didn't work!");
               }
});
 */






