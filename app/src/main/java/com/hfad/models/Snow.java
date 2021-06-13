package com.hfad.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Snow {
    @JsonProperty("1h") double OneHour;
    @JsonProperty("3h") double ThreeHour;
}
