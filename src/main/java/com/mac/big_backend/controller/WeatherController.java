package com.mac.big_backend.controller;

import com.mac.big_backend.dto.CityRequest;
import com.mac.big_backend.dto.WeatherResponse;
import com.mac.big_backend.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/weather")
@RestController
public class WeatherController {

    private final WeatherService weatherService;
    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/health-check")
    public String returnHello(){
        log.info(System.getenv("WEATHER_API_KEY"));
        return "Happy Weather in big-backend!";
    }

    @PostMapping(value = "/temperature", consumes = "application/json", produces = "application/json")
    public WeatherResponse getTemperture(@RequestBody CityRequest cityRequest){
        return weatherService.getTemperature(cityRequest.getCity());
    }
}
