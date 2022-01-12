package ch.roomManager.dao.specified;

import ch.roomManager.dao.Result;
import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDao implements Dao<Room, Integer> {

  @Override
  public List<Room> getAll() {
    List<Room> roomList = new ArrayList<>();
    String sqlQuery = "SELECT roomId, name, description FROM room;";
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      while (resultSet.next()) {
        roomList.add(Room.builder()
            .id(resultSet.getInt(1))
            .name(resultSet.getString(2))
            .description(resultSet.getString(3))
            .build());
      }
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return roomList;
  }

  @Override
  public Room getEntity(Integer roomId) {
    Map<Integer, Integer> values = new HashMap<>();
    String sqlQuery = "SELECT roomId, name, description FROM room WHERE roomId=?;";
    values.put(1, roomId);
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
      if (resultSet.next()) {
        return Room.builder()
            .id(resultSet.getInt(1))
            .name(resultSet.getString(2))
            .description(resultSet.getString(3))
            .build();
      }

    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return Room.builder().build();
  }

  @Override
  public Result save(Room room) {
    String sqlQuery = "REPLACE room SET roomId=?, name=?, description=?;";

    try {
      MySqlDB.setPrepStmt(sqlQuery).setInt(1, room.getId());
      MySqlDB.getPrepStmt().setString(2, room.getName());
      MySqlDB.getPrepStmt().setString(3, room.getDescription());

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
    String sqlQuery = "DELETE FROM room WHERE eventId=?;";
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
    String sqlQuery = "SELECT COUNT(*) FROM room;";
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
