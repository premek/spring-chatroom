package net.vyhnal.demo.message;

import net.vyhnal.demo.NotFoundException;
import net.vyhnal.demo.room.RoomEntity;
import net.vyhnal.demo.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private Clock clock;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MessageMapper messageMapper;

    public List<MessageEntity> getLastMessages(String roomName) {
        RoomEntity room = roomRepository
                .findByName(roomName)
                .orElseThrow(NotFoundException::new);

        return messageRepository
                .findFirst10ByRoomOrderByPostedDesc(room) // top 10, newest first
                .stream()
                .sorted(Comparator.comparing(MessageEntity::getPosted)) // sort oldest to newest
                .toList();
    }

    public MessageEntity saveMessage(MessageDto message, String roomName) {
        RoomEntity room = roomRepository
                .findByName(roomName)
                .orElseThrow(NotFoundException::new);

        Instant now = Instant.now(clock);
        MessageEntity messageEntity = messageMapper.dtoToEntity(message, room, now);
        return messageRepository.save(messageEntity);
    }
}