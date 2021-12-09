package ch.roomManager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Room {
    private int id;
    private String name;
    private String description;

    public void printString() {
        System.out.println("Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}');
    }
}
