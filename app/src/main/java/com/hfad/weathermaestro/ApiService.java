package com.hfad.weathermaestro;

import android.app.AlertDialog;
import android.content.Context;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfad.models.*;
import com.android.volley.*;

import java.io.IOException;
import java.net.URL;

public class ApiService {
    private static final String API_KEY = "48a518fb409cd12339687e425797561c";
    private static final String API_ADDRESS = "http://api.openweathermap.org/data/2.5/";
    private static ApiService instance;
    //private final Context context;
    //private final RequestQueue queue;


    public static ApiService getInstance(Context context) { // always use this to assign an ApiService variable, never use new
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    public ApiService(Context context) {
        //this.context = context;
        //this.queue = Volley.newRequestQueue(context);
    }

    public ApiService() {}

    public CurrentWeather GetCurrentWeather(Location location) {

        CurrentWeather currentWeather = null;
        String URI = API_ADDRESS + "weather?q=";
        String query = "";
        //final String[] response = new String[1];

        if (location.city != null && !location.city.equals("")) { //if the location object specifies a city, then use that
            query += location.city;

            if (location.stateCode != null && !location.stateCode.equals("")) { //maybe add country?
                query += "," + location.stateCode;
            }
        }
        else if (location.latitude != null && location.longitude != null) { // if no city is specified, use geolocation
            query += "lat=" + location.latitude + "&lon=" + location.longitude;
        }
        if (!query.equals("")) {
            query += "&appid=" + API_KEY;

//            StringRequest request = new StringRequest(Request.Method.GET, URI + query, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String stringResponse) {
//                    response[0] = stringResponse;
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    new AlertDialog.Builder(context).setTitle("Error").setMessage(error.getMessage()).show();
//                }
//            });
//
//            queue.add(request);
            ObjectMapper mapper = new ObjectMapper();

            try {
                currentWeather = mapper.readValue(new URL(URI+query),CurrentWeather.class);// try grabbing the URL without Volley??
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return currentWeather;
    }
}
