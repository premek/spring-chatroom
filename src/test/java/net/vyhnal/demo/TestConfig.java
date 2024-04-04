package net.vyhnal.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
public class TestConfig {
    public static final Instant fixedInstant = Instant.parse("2024-12-24T10:00:00Z");

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(fixedInstant, ZoneOffset.UTC);
    }
}
