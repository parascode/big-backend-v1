package com.mac.big_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mac.big_backend.configuration.RedisConfig;
import com.mac.big_backend.configuration.WebClientConfig;
import com.mac.big_backend.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class WeatherService {

    private WebClient webClient;
    private RedisTemplate<Object, Object> redisTemplate;
    private ObjectMapper mapper;

    @Autowired
    public WeatherService(WebClient webClient, RedisTemplate<Object, Object> redisTemplate, ObjectMapper mapper){
        this.webClient = webClient;
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    public WeatherResponse getTemperature(String city){

        Object cachedResponse = redisTemplate.opsForValue().get(city);
        if(cachedResponse != null) {
            try {
                log.info((String) cachedResponse);
                WeatherResponse response = mapper.readValue(cachedResponse.toString(), WeatherResponse.class);
                log.info("got value from redis cache, {}, {}", city, response);
                return response;
            }catch (Exception e){
                log.error("Error reading from redis cache: {}", e.getMessage());
                return null;
            }
        }
        else{
            WeatherResponse weatherResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", System.getenv("WEATHER_API_KEY"))
                            .queryParam("q", city)
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();
            if(weatherResponse != null) {
                try {
                    String stringResponse = mapper.writeValueAsString(weatherResponse);
                    log.info("saving to redis cache, {}, {}", city, stringResponse);
                    redisTemplate.opsForValue().set(city, stringResponse, 300, java.util.concurrent.TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
            return weatherResponse;
        }
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/current.json")
//                        .queryParam("key", System.getenv("WEATHER_API_KEY"))
//                        .queryParam("q", city)
//                        .build())
//                .retrieve()
//                .bodyToMono(WeatherResponse.class)
//                .block();
    }
}
