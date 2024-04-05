package net.vyhnal.demo.weather;

import java.util.List;

public record WeatherResponse(Current current) {
    public record Current(double temperature_2m){}
}
