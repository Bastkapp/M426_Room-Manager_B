package ch.roomManager.dao;

import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Event;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDAO implements Dao<Event, Integer> {

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

    } catch (SQLException sqlEx) {

      sqlEx.printStackTrace();
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return Event.builder().build();
  }
}
