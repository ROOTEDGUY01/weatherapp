package com.example.weather_app.controller;

import com.example.weather_app.model.WeatherResponce;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class WheatherController {
    @Value("${api.key}")
    private String appkey;

    @GetMapping("/")
    public String getindex() {
        return "index";
    }

    @GetMapping("/weather")
    public String getweather(@RequestParam("city") String city, Model model) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appId=" + appkey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();

        try {
            WeatherResponce weatherResponce = restTemplate.getForObject(url, WeatherResponce.class);
            if (weatherResponce != null) {
                model.addAttribute("city", weatherResponce.getName());
                model.addAttribute("country", weatherResponce.getSys().getCountry());
                model.addAttribute("weatherDescription", weatherResponce.getWeather().get(0).getDescription());
                model.addAttribute("temperature", weatherResponce.getMain().getTemp());
                model.addAttribute("humidity", weatherResponce.getMain().getHumidity());
                model.addAttribute("windSpeed", weatherResponce.getWind().getSpeed());
                String weatherIcon = "wi wi-owm-" + weatherResponce.getWeather().get(0).getId();
                model.addAttribute("weatherIcon", weatherIcon);
            }
        } catch (Exception e) {
            model.addAttribute("error", "City not found");
        }

        return "weather";
    }
}

