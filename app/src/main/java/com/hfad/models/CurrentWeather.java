package com.hfad.models;

import java.util.ArrayList;
import java.util.List;

// Object that holds a deserialized API response
public class CurrentWeather {
    public Coord coord;
    public List<Weather> weather = new ArrayList<>();
    public String base;
    public Main main;
    public int visibility; // no idea what unit this is
    public Wind wind;
    public Clouds clouds;
    public Rain rain;
    public Snow snow;
    public long dt; // utc unix time
    public Sys sys;
    public long timezone; // timezone offset, add to dt to get the time in the local timezone
    public int id; // dunno what this is for
    public String name; // location name, I think usually the city
    public int cod; // not sure what this is
}
