package com.hfad.models;

public class Wind {
    public double speed; // m/s in metric, mph in imperial
    public int deg; // direction of wind in degrees (meteorological)
    public Double gust; // Double because it might be null
}
