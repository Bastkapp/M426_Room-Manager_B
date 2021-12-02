package ch.roomManager.models;

import lombok.Builder;
import lombok.Data;

    @Data
    @Builder(toBuilder = true)
    public class Room {
        private final int id;
        private String name;
        private String description;

        public Room(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }

