package com.hfad.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {
    @JsonProperty("1h") double OneHour;
    @JsonProperty("3h") double ThreeHour;
}
