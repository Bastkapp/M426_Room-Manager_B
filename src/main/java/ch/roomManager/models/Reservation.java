package ch.roomManager.models;

import ch.roomManager.dao.annotations.Id;
import ch.roomManager.dao.annotations.SubId;
import ch.roomManager.dao.annotations.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Table(name = "reservation")
public class Reservation {

  @Id
  private int id;
  private LocalDate start;
  private LocalDate end;

  @SubId
  private Event event;

  @SubId
  private Room room;
}
