package com.hfad.weathermaestro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfad.models.*;
import java.io.IOException;
import java.net.URL;

public class ApiService {
    private static final String API_KEY = "48a518fb409cd12339687e425797561c";
    private static final String API_ADDRESS = "http://api.openweathermap.org/data/2.5/";

    /* * *
     *  Returns a deserialized API response that contains current weather details for
     *  the given location.
     *
     *  @param  location    Location object containing either a city and state, or coordinates
     *  @param  units       Either F for imperial units or C for metric. Anything else will default to kelvin. Might change that.
     *  @return             An object representing the API response
     */
    public static CurrentWeather GetCurrentWeather(Location location, String units) {
        CurrentWeather currentWeather = null;
        String URI = API_ADDRESS + "weather?q=";
        String query = "";

        //if the location object specifies a city, then use that
        if (location.city != null && !location.city.equals("")) {
            query += location.city;

            // add state code to the query if it exists
            if (location.stateCode != null && !location.stateCode.equals("")) {
                query += "," + location.stateCode;
            }
        }
        // if no city is specified, use geolocation
        else if (location.latitude != null && location.longitude != null) {
            query += "lat=" + location.latitude + "&lon=" + location.longitude;
        }
        // if a location was successfully set, continue
        if (!query.equals("")) {
            query += "&appid=" + API_KEY; // adds the API key to the query

            // check for preferred units and add them to the query string
            if (units != null) {
                if (units.toUpperCase().equals("F")) {
                    query += "&units=imperial";
                }
                else if (units.toUpperCase().equals("C")) {
                    query += "&units=metric";
                }
            }
            // ObjectMapper turns a JSON response into java objects
            ObjectMapper mapper = new ObjectMapper();
            try {
                //builds the URL and opens a stream for deserialization, then puts the result into the return object
                currentWeather = mapper.readValue(new URL(URI+query),CurrentWeather.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return currentWeather;
    }
}
