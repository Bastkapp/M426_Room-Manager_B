package ch.roomManager.dao.specified;

import ch.roomManager.dao.Result;
import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDao implements Dao<Event, Integer> {

  @Override
  public List<Event> getAll() {
    List<Event> eventList = new ArrayList<>();
    String sqlQuery = "SELECT eventId, title, description, organiser FROM event;";
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      while (resultSet.next()) {
        eventList.add(Event.builder()
            .id(resultSet.getInt(1))
            .title(resultSet.getString(2))
            .description(resultSet.getString(3))
            .organiser(resultSet.getString(4))
            .build());
      }
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return eventList;
  }


  @Override
  public Event getEntity(Integer eventId) {
    Map<Integer, Integer> values = new HashMap<>();
    String sqlQuery = "SELECT eventId, title, description, organiser FROM event WHERE eventId=?;";
    values.put(1, eventId);
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
      if (resultSet.next()) {
        return Event.builder()
            .id(resultSet.getInt(1))
            .title(resultSet.getString(2))
            .description(resultSet.getString(3))
            .organiser(resultSet.getString(4))
            .build();
      }
      return null;
    } catch (SQLException sqlEx) {

      sqlEx.printStackTrace();
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
  }

  @Override
  public Result save(Event event) {
    String sqlQuery = "REPLACE event SET eventId=?, title=?, description=?, organiser=?;";
    try {
      MySqlDB.setPrepStmt(sqlQuery).setInt(1, event.getId());
      MySqlDB.getPrepStmt().setString(2, event.getTitle());
      MySqlDB.getPrepStmt().setString(3, event.getDescription());
      MySqlDB.getPrepStmt().setString(4, event.getOrganiser());

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
    String sqlQuery = "DELETE FROM event WHERE eventId=?;";
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
    String sqlQuery = "SELECT COUNT(*) FROM event;";
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
