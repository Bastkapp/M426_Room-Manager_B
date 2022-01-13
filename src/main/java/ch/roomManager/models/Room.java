package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@Table(name = "room")
public class Room {

  @Id
  private int id;
  private String name;
  private String description;
}
