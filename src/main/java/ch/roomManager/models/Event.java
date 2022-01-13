package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@Table(name = "event")
public class Event {

  @Id
  private int id;
  private String title;
  private String description;
  private String organiser;
}
