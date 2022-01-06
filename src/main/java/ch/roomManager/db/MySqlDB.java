package ch.roomManager.db;

import ch.roomManager.service.Config;
import ch.roomManager.dao.Result;

import java.sql.*;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MySqlDB {

  private static Connection connection = null;
  private static PreparedStatement prepStmt;
  private static ResultSet resultSet;
  private static Result result = null;

  private MySqlDB() {
  }

  public static synchronized ResultSet sqlSelect(String sqlQuery) throws SQLException {
    setPrepStmt(sqlQuery);
    return sqlSelect();
  }

  public static ResultSet sqlSelect(String sqlQuery, Map<Integer, ?> values) throws SQLException {
    setPrepStmt(sqlQuery, values);
    return MySqlDB.sqlSelect();
  }

  public static synchronized ResultSet sqlSelect() throws SQLException {
    setResultSet(null);
    try {
      setResultSet(getPrepStmt().executeQuery());
    } catch (SQLException sqlException) {
      printSQLException(sqlException);
      throw sqlException;
    }
    return getResultSet();
  }

  public static Result sqlUpdate(String sqlQuery) throws SQLException {
    setPrepStmt(sqlQuery);
    return MySqlDB.sqlUpdate();
  }

  public static Result sqlUpdate(String sqlQuery, Map<Integer, ?> values) throws SQLException {
    setPrepStmt(sqlQuery, values);
    return MySqlDB.sqlUpdate();
  }

  public static Result sqlUpdate() throws SQLException {
    try {

      if (prepStmt == null) return Result.ERROR;
      int affectedRows = getPrepStmt().executeUpdate();
      if (affectedRows <= 2) {
        return Result.SUCCESS;
      } else if (affectedRows == 0) {
        return setResult(Result.NOACTION);
      } else {
        return setResult(Result.ERROR);
      }
    } catch (SQLException sqlException) {
      String sqlState = sqlException.getSQLState();
      if (sqlState.equals("23000")) {
        return setResult(Result.DUPLICATE);
      } else {
        printSQLException(sqlException);
        throw sqlException;
      }
    } finally {
      sqlClose();
    }
  }

  private static void setPrepStmt(String sqlQuery, Map<Integer, ?> values) throws SQLException {
    setPrepStmt(sqlQuery);
    if (values != null) {
      setPrepStmt(values);
    }
  }

  private static void setPrepStmt(Map<Integer, ?> values) throws SQLException {

    for (Integer i = 1; values.containsKey(i); i++) {
      if (values.get(i).getClass() == Integer.class)
        getPrepStmt().setInt(i, (int) values.get(i));
      else if (values.get(i).getClass() == String.class)
        getPrepStmt().setString(i, (String) values.get(i));
    }
  }

  public static void sqlClose() {
    try {
      if (getResultSet() != null) {
        getResultSet().close();
      }
      if (getPrepStmt() != null) {
        getPrepStmt().close();
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  static void printSQLException(SQLException sqlEx, String sqlQuery) {
    System.out.println("Query: " + sqlQuery);
    System.err.println("Query: " + sqlQuery);
    printSQLException(sqlEx);
  }

  public static void printSQLException(SQLException sqlEx) {
    StringBuilder message = new StringBuilder("ERROR: an SQLException has occured");
    for (Throwable exception : sqlEx) {
      if (exception instanceof SQLException) {

        exception.printStackTrace(System.err);
        message.append("\nSQLState: ").append(((SQLException) exception).getSQLState());
        message.append("\nError Code: ").append(((SQLException) exception).getErrorCode());
        message.append("\nMessage: ").append(exception.getMessage());

        Throwable cause = sqlEx.getCause();
        while (cause != null) {
          message.append("\nCause: ").append(cause);
          cause = cause.getCause();
        }
      }
    }
    System.out.println(message);
    System.err.println(message);
  }

  public static Connection getConnection() {
    try {
      if (connection == null || !connection.isValid(2)) {
        InitialContext initialContext = new InitialContext();
        DataSource dataSource = (DataSource) initialContext.lookup(
            Config.getProperty("jdbcRessource")
        );
        setConnection(dataSource.getConnection());
      }
    } catch (NamingException | SQLException namingException) {
      namingException.printStackTrace();
      throw new RuntimeException();

    }

    return connection;
  }

  private static Connection setConnection(Connection connection) {
    MySqlDB.connection = connection;
    return getConnection();
  }

  public static PreparedStatement getPrepStmt() {
    return prepStmt;
  }

  public static PreparedStatement setPrepStmt(PreparedStatement prepStmt) {
    MySqlDB.prepStmt = prepStmt;
    return getPrepStmt();
  }

  public static PreparedStatement setPrepStmt(String prepStmt) throws SQLException {
    getConnection().prepareStatement(prepStmt);
    return getPrepStmt();
  }

  public static ResultSet getResultSet() {
    return resultSet;
  }

  public static ResultSet setResultSet(ResultSet resultSet) {
    MySqlDB.resultSet = resultSet;
    return getResultSet();
  }

  public static Result setResult(Result result) {
    MySqlDB.result = result;
    return MySqlDB.getResult();
  }

  public static Result setResult(String result) {
    return MySqlDB.setResult(Result.valueOf(result));
  }

  public static Result getResult() {
    return result;
  }
}
