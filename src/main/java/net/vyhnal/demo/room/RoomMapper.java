package net.vyhnal.demo.room;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RoomMapper {
    RoomDto entityToDto(RoomEntity message);
}