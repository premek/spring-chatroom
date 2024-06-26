package net.vyhnal.demo.room;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "ROOM")
public class RoomEntity {
    @Id
    private int id;
    private String name;
    private String label;

    public RoomEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}