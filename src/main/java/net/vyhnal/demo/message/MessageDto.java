package net.vyhnal.demo.message;

import java.time.Instant;

public record MessageDto(String username, String text, Instant posted) {
}
