package ch.roomManager.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Room {
    private int id;
    private String name;
    private String description;
}
