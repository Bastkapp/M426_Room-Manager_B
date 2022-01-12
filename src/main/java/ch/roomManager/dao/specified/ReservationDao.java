package ch.roomManager.dao.specified;

import ch.roomManager.dao.Result;
import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDao implements Dao<Reservation, Integer> {

  @Override
  public List<Reservation> getAll() {
    List<Reservation> reservationList = new ArrayList<>();
    String sqlQuery = "SELECT reservationId, eventId, roomId, start, end FROM reservation;";
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      while (resultSet.next()) {
        reservationList.add(Reservation.builder()
            .id(resultSet.getInt(1))
            .event(new EventDao().getEntity(resultSet.getInt(2)))
            .room(new RoomDao().getEntity(resultSet.getInt(3)))
            .start(resultSet.getDate(4).toLocalDate())
            .end(resultSet.getDate(5).toLocalDate())
            .build());
      }
      return reservationList;
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
  }

  @Override
  public Reservation getEntity(Integer reservationId) {
    Map<Integer, Integer> values = new HashMap<>();
    String sqlQuery = "SELECT reservationId, eventId, roomId, start, end FROM reservation;";
    values.put(1, reservationId);
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      if (resultSet.next()) {
        return Reservation.builder()
            .id(resultSet.getInt(1))
            .event(new EventDao().getEntity(resultSet.getInt(2)))
            .room(new RoomDao().getEntity(resultSet.getInt(3)))
            .start(resultSet.getDate(4).toLocalDate())
            .end(resultSet.getDate(5).toLocalDate())
            .build();
      }
      return null;
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
  }

  @Override
  public Result save(Reservation reservation) {
    String sqlQuery = "REPLACE reservation SET reservationId=?, eventId=?, roomId=?, start=?, end=?;";
    try {
      MySqlDB.setPrepStmt(sqlQuery).setInt(1, reservation.getId());
      MySqlDB.getPrepStmt().setInt(2, reservation.getEvent().getId());
      MySqlDB.getPrepStmt().setInt(3, reservation.getRoom().getId());

      MySqlDB.sqlUpdate();
    } catch (SQLException e) {
      MySqlDB.printSQLException(e);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return MySqlDB.getResult();
  }

  @Override
  public Result delete(Integer id) {
    String sqlQuery = "DELETE FROM reservation WHERE reservationId=?;";
    Map<Integer, Integer> values = new HashMap<>();
    values.put(1, id);
    try {
      MySqlDB.sqlUpdate(sqlQuery, values);
    } catch (SQLException e) {
      MySqlDB.printSQLException(e);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return MySqlDB.getResult();
  }

  @Override
  public Integer count() {
    String sqlQuery = "SELECT COUNT(*) FROM reservation;";
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
      return -1;
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
  }
}
