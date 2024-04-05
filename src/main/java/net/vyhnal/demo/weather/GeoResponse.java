package net.vyhnal.demo.weather;

import java.util.List;

public record GeoResponse(List<GeoResult> results) {
    public record GeoResult(double latitude, double longitude) {
    }
}
