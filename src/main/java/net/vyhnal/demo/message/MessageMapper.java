package net.vyhnal.demo.message;

import net.vyhnal.demo.room.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import java.time.Instant;

@Mapper(componentModel = ComponentModel.SPRING)
public interface MessageMapper {
    MessageDto entityToDto(MessageEntity message);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", source = "room")
    @Mapping(target = "posted", source = "posted")
    MessageEntity dtoToEntity(MessageDto messageDto, RoomEntity room, Instant posted);
}