package com.mac.big_backend.dto;

import lombok.Data;

@Data
public class WeatherResponse {
    public Location location;
    public Current current;
}
