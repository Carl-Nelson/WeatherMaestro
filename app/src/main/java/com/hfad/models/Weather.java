package com.hfad.models;

import com.hfad.weathermaestro.R;

public class Weather {
    public int id;
    public String main; // name of weather type, ie. clear
    public String description; // ex. "clear sky"
    public int icon; // filename of weather icon ie. "01d" for clear

    //get weather icons from drawables
    public static final Weather[] weathers = {
            new Weather("Clear", "clear sky",
                    R.drawable.d01),
            new Weather("Clouds", "few clouds",
                    R.drawable.d02),
            new Weather("Clouds", "scattered clouds",
                    R.drawable.d03),
            new Weather("Clouds", "broken clouds",
                    R.drawable.d04),
            new Weather("Rain", "shower rain",
                    R.drawable.d09),
            new Weather("Rain", "rain",
                    R.drawable.d10),
            new Weather("Thunderstorm", "thunderstorm",
                    R.drawable.d11),
            new Weather("Snow", "snow",
                    R.drawable.d13),
            new Weather("Mist", "mist",
                    R.drawable.d50)
    };

    //Each Weather has a main(name), description, and an icon image
    private Weather(String main, String description, int icon) {
        this.main = main;
        this.description = description;
        this.icon = icon;
    }
    public String getDescription() {
        return description;
    }
    public String getMain() {
        return main;
    }
    public int getIcon() {
        return icon;
    }
    public String toString() {
        return this.main;
    }
}
