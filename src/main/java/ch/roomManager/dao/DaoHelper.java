package ch.roomManager.dao;

import ch.roomManager.db.MySqlDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DaoHelper {
  static Result doDelete(Integer integer, String type) {
    String sqlQuery = "DELETE FROM ? WHERE ?=?;";
    try {
      MySqlDB.setPrepStmt(sqlQuery).setString(1, TableName.valueOf(type).toString());
      MySqlDB.getPrepStmt().setString(2, IdName.valueOf(type).toString());
      MySqlDB.getPrepStmt().setInt(3, integer);

      MySqlDB.sqlUpdate();
    } catch (SQLException e) {
      MySqlDB.printSQLException(e);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return MySqlDB.getResult();
  }

  public static Integer doCount(String type) {
    Map<Integer, String> values = new HashMap<>();
    String sqlQuery = "SELECT COUNT(*) FROM ?;";
    values.put(1, IdName.valueOf(type).toString());
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }

    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return -1;
  }

  private enum IdName {
    ROOM("roomId"),
    EVENT("eventId"),
    RESERVATION("reservationId");

    private String idName;

    IdName(String idName) {
      this.setIdName(idName);
    }

    public void setIdName(String idName) {
      this.idName = idName;
    }

    public String toString() {
      return idName;
    }
  }

  private enum TableName {
    ROOM("room"),
    EVENT("event"),
    RESERVATION("reservation");

    private String tableName;

    TableName(String tableName) {
      this.setTableName(tableName);
    }

    public void setTableName(String tableName) {
      this.tableName = tableName;
    }

    @Override
    public String toString() {
      return tableName;
    }
  }
}
