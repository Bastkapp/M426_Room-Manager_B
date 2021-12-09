package ch.roomManager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * Close resultSet and prepared statement
     */
    static void sqlClose() {
        try {
            if (getResultSet() != null) getResultSet().close();
            if (getPrepStmt() != null) getPrepStmt().close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    /**
     * Gets the connection: open new connection if needed
     *
     * @return connection
     */
    static Connection getConnection() throws SQLException {
        String url = MySqlDB.class.getResource("db.url");
        return DriverManager.getConnection(url, "user", "password");
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
