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
}
