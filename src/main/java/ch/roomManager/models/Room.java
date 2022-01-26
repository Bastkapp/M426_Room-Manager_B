package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

import javax.ws.rs.FormParam;

@Data
@Builder(toBuilder = true)
@Table(name = "room")
public class Room {

  @Id
  private int id;

  @FormParam("name")
  private String name;

  @FormParam("description")
  private String description;
}
