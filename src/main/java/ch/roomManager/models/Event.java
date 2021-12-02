package ch.roomManager.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Event {
    private final int id;
    private String title;
    private String description;
    private String organiser;

    public Event(int id, String title, String description, String organiser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organiser = organiser;
    }
}
