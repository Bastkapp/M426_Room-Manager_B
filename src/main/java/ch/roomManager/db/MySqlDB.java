package ch.roomManager.db;

import ch.roomManager.service.Config;
import ch.roomManager.dao.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * common methods for MySQL database
 * <p>
 * M151: BookshelfDB
 *
 * @author Marcel Suter
 */

public class MySqlDB {

  private static Connection connection = null;
  private static PreparedStatement prepStmt;
  private static ResultSet resultSet;

  /**
   * default constructor: defeat instantiation
   */
  private MySqlDB() {
  }

  /**
   * Execute SELECT-Query without dynamic values and return the resultset
   *
   * @param sqlQuery the query to be executed
   * @return ResultSet the data retrieved from the database
   * @throws SQLException Syntax errors
   */
  public static synchronized ResultSet sqlSelect(String sqlQuery)
      throws SQLException {
    return sqlSelect(sqlQuery, null);
  }

  /**
   * execute SELECT-query with dynamic values and return the resultset
   *
   * @param sqlQuery the query to be executed
   * @param values   map of values to be inserted
   * @return ResultSet the data retrieved from the database
   * @throws SQLException Syntax errors
   */
  public static synchronized ResultSet sqlSelect(String sqlQuery, Map<Integer, Integer> values) throws SQLException {
    setResultSet(null);

    try {
      setPrepStmt(getConnection().prepareStatement(sqlQuery));

      if (values != null) {
        setValues(values);
      }
      setResultSet(getPrepStmt().executeQuery());

    } catch (SQLException sqlException) {
      printSQLException(sqlException, sqlQuery);
      throw sqlException;
    }
    return getResultSet();
  }

  /**
   * execute a query without dynamic values to update the db (INSERT, UPDATE, DELETE, REPLACE)
   *
   * @param sqlQuery the query to be executed
   * @return number of affected rows
   * @throws SQLException
   */
  static Result sqlUpdate(String sqlQuery) throws SQLException {
    return sqlUpdate(sqlQuery, null);
  }

  /**
   * execute a query with dynamic values to update the db (UPDATE, DELETE, REPLACE)
   *
   * @param sqlQuery the query to be executed
   * @param values   map of values to be inserted
   * @return number of affected rows
   * @throws SQLException
   */
  static Result sqlUpdate(String sqlQuery, Map<Integer, String> values) throws SQLException {
    try {
      setPrepStmt(getConnection().prepareStatement(sqlQuery));

      if (values != null) {
        setValues(values);
      }
      int affectedRows = getPrepStmt().executeUpdate();
      if (affectedRows <= 2) {
        return Result.SUCCESS;
      } else if (affectedRows == 0) {
        return Result.NOACTION;
      } else {
        return Result.ERROR;
      }
    } catch (SQLException sqlException) {
      String sqlState = sqlException.getSQLState();
      if (sqlState.equals("23000")) {
        return Result.DUPLICATE;
      } else {
        printSQLException(sqlException);
        throw sqlException;
      }
    } finally {
      sqlClose();
    }
  }

  /**
   * Insert the values into a prepared-Statement
   *
   * @param values map with attributename=value
   * @throws SQLException wrong parameter count
   */

  private static void setValues(Map<Integer, ?> values) throws SQLException {

    for (Integer i = 1; values.containsKey(i); i++) {
      if (values.get(i).getClass() == Integer.class)
        getPrepStmt().setInt(i, (int) values.get(i));
      else if (values.get(i).getClass() == String.class)
        getPrepStmt().setString(i, (String) values.get(i));
    }
  }

  /**
   * Close resultSet and prepared statement
   */
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

  /**
   * Show query, error codes and messages for a SQL-Exception
   *
   * @param sqlEx    the SQLException
   * @param sqlQuery the executed query
   */
  static void printSQLException(SQLException sqlEx, String sqlQuery) {
    System.out.println("Query: " + sqlQuery);
    System.err.println("Query: " + sqlQuery);
    printSQLException(sqlEx);
  }

  /**
   * Show error codes and messages for a SQL-Exception
   *
   * @param sqlEx the SQLException
   */
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


  /**
   * Gets the connection: open new connection if needed
   *
   * @return connection
   */
  public static Connection getConnection() {
    try {
      if (connection == null || !connection.isValid(2)) {
        InitialContext initialContext = new InitialContext();
        DataSource dataSource = (DataSource) initialContext.lookup(
            Config.getProperty("jdbcRessource")
        );
        setConnection(dataSource.getConnection());
      }
    } catch (NamingException namingException) {
      namingException.printStackTrace();
      throw new RuntimeException();

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new RuntimeException();
    }

    return connection;
  }

  /**
   * Sets the connection
   *
   * @param connection the value to set
   */

  private static void setConnection(Connection connection) {
    MySqlDB.connection = connection;
  }

  /**
   * Gets the prepStmt
   *
   * @return value of prepStmt
   */
  private static PreparedStatement getPrepStmt() {
    return prepStmt;
  }

  /**
   * Sets the prepStmt
   *
   * @param prepStmt the value to set
   */

  public static void setPrepStmt(PreparedStatement prepStmt) {
    MySqlDB.prepStmt = prepStmt;
  }

  /**
   * Gets the resultSet
   *
   * @return value of resultSet
   */
  public static ResultSet getResultSet() {
    return resultSet;
  }

  /**
   * Sets the resultSet
   *
   * @param resultSet the value to set
   */

  public static void setResultSet(ResultSet resultSet) {
    MySqlDB.resultSet = resultSet;
  }
}
