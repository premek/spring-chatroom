package net.vyhnal.demo.message;

import net.vyhnal.demo.room.RoomEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    @Cacheable(value = "messages", key = "#room")
    List<MessageEntity> findFirst10ByRoomOrderByPostedDesc(RoomEntity room);


    @CacheEvict(value = "messages", key = "#message.room")
    @Override
    MessageEntity save(MessageEntity message);
}