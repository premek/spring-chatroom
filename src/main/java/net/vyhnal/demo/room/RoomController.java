package net.vyhnal.demo.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoomController {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDto> getAll() {
        return roomService
                .getAll()
                .stream()
                .map(roomMapper::entityToDto)
                .toList();
    }
}
