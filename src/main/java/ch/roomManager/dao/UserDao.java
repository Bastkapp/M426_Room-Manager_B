package ch.roomManager.dao;

import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {
    public User getEntity(String username, String password) {
        String sqlQuery = "SELECT id, username FROM user WHERE username=?, password=?;";
        Map<Integer, String> values = new HashMap<>();
        values.put(1, username);
        values.put(2, password);
        try {
            ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
            if (resultSet.next()) {
                return User.builder()
                    .id(resultSet.getInt(1))
                    .username(resultSet.getString(2))
                    .build();
            }
        } catch (SQLException sqlEx) {
            MySqlDB.printSQLException(sqlEx);
            throw new RuntimeException();
        } finally {
            MySqlDB.sqlClose();
        }
        return null;
    }
}
