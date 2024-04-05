package net.vyhnal.demo.weather;

import net.vyhnal.demo.message.MessageDto;
import net.vyhnal.demo.weather.GeoResponse.GeoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
public class WeatherBotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Clock clock;
    public GeoResult getCoord(String locationName) {
        ResponseEntity<GeoResponse> response = restTemplate.getForEntity(
                "https://geocoding-api.open-meteo.com/v1/search?name="+locationName,
                GeoResponse.class);
        return response.getBody().results().stream().findFirst().orElseThrow(); // TODO
    }
    public double getWeather(GeoResult location) {
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(
                "https://api.open-meteo.com/v1/forecast?latitude="+location.latitude()
                        +"&longitude="+location.longitude()
                        +"&current=temperature_2m",
                WeatherResponse.class);
        return response.getBody().current().temperature_2m(); // TODO
    }

    public Optional<MessageDto> respond(MessageDto message) {
        if (!message.text().startsWith("@weather")) {
            return Optional.empty();
        }
        String[] parts = message.text().split(" ");
        String locationName = parts[1];
        return Optional.of(new MessageDto("weatherbot",
                "Temperature in "+locationName +": "+getWeather(getCoord(locationName))+"C",
                Instant.now(clock)));
    }
}