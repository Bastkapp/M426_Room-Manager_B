package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.SubId;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

import javax.ws.rs.FormParam;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Table(name = "reservation")
public class Reservation {

  @Id
  private int id;

  @FormParam("start")
  private LocalDateTime start;

  @FormParam("end")
  private LocalDateTime end;

  @SubId
  private Event event;

  @SubId
  private Room room;
}
