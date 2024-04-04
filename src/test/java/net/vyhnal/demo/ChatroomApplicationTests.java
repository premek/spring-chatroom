package net.vyhnal.demo;

import net.vyhnal.demo.message.MessageDto;
import net.vyhnal.demo.room.RoomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
public class ChatroomApplicationTests {
    @Autowired
    private TestRestTemplate rest;

    @Test
    void rooms() {
        var resp = rest.exchange(
                "/api/v1/rooms",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoomDto>>() {
                });

        assertThat(resp.getBody()).isEqualTo(List.of(
                new RoomDto("music", "Music"),
                new RoomDto("movies", "Movies"),
                new RoomDto("gaming", "Gaming")));
    }

    @Test
    void nonexistentRoom() {
        var resp = rest.getForEntity("/api/v1/messages/nonexistent", Object.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void messages() {
        assertMessages(List.of());
        insertMessage(new MessageDto("user1", "hello", null));
        assertMessages(List.of(new MessageDto("user1", "hello", TestConfig.fixedInstant)));
    }

    private void insertMessage(MessageDto message) {
        var resp = rest.postForEntity("/api/v1/messages/music", message, Object.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    private void assertMessages(List<MessageDto> expected) {
        var resp = rest.exchange(
                "/api/v1/messages/music",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MessageDto>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo(expected);
    }


}