package ch.roomManager.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder(toBuilder = true)
public class Reservation {
    private int id;
    private int eventid;
    private int roomid;
    private Date start;
    private Date end;
}