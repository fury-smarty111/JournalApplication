package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.Api_Response.WeatherResponse;
import com.Unemployment.JournalApplication.Cache.AppCache;
import com.Unemployment.JournalApplication.Constants.PlaceHolders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        // 1. Check Redis Cache (Distributed Cache)
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            log.info("Cache hit for city: {}", city);
            return weatherResponse;
        }

        // 2. Fetch API URL Template from AppCache (In-memory/Config Cache)
        String apiUrlTemplate = appCache.get(AppCache.Keys.WEATHER_API.name());

        if (apiUrlTemplate == null) {
            log.error("Weather API template not found in AppCache for key: {}", AppCache.Keys.WEATHER_API.name());
            return null;
        }

        try {
            // 3. Construct the final URL
            String finalAPI = apiUrlTemplate
                    .replace(PlaceHolders.CITY, city)
                    .replace(PlaceHolders.API_KEY, apiKey);

            // 4. External API Call
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();

            // 5. Update Redis Cache (5-minute TTL)
            if (body != null) {
                redisService.set("weather_of_" + city, body, 300L);
            }
            return body;

        } catch (Exception e) {
            log.error("Error fetching weather for city {}: ", city, e);
            return null;
        }
    }
}