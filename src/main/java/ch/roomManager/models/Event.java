package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

import javax.ws.rs.FormParam;

@Data
@Builder(toBuilder = true)
@Table(name = "event")
public class Event {

  @Id
  private int id;

  @FormParam("title")
  private String title;

  @FormParam("description")
  private String description;

  @FormParam("organiser")
  private String organiser;
}
