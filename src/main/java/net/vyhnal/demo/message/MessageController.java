package net.vyhnal.demo.message;

import net.vyhnal.demo.weather.WeatherBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MessageController {
    @Autowired
    private SimpMessagingTemplate websocket;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WeatherBotService weatherBotService;

    @PostMapping("/rooms/{room}/messages")
    public void sendMessage(@PathVariable String room, @RequestBody MessageDto message) {
        MessageDto savedDto = saveAndBroadcast(room, message);

        weatherBotService.respond(savedDto).ifPresent(botMessage -> {
            saveAndBroadcast(room, botMessage);
        });
    }

    private MessageDto saveAndBroadcast(String room, MessageDto message) {
        MessageEntity savedEntity = messageService.saveMessage(message, room);
        MessageDto savedDto = messageMapper.entityToDto(savedEntity);
        websocket.convertAndSend("/topic/" + room, savedDto);
        return savedDto;
    }

    @GetMapping("/rooms/{room}/messages")
    public List<MessageDto> getHistory(@PathVariable String room) {
        return messageService
                .getLastMessages(room)
                .stream()
                .map(messageMapper::entityToDto)
                .toList();
    }
}
