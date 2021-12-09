package ch.roomManager.dao;

import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements Dao<Room, Integer> {
    @Override
    public List<Room> getAll() {
        Connection connection;
        PreparedStatement prepStmt;
        ResultSet resultSet;
        List<Room> roomList = new ArrayList<>();
        String sqlQuery = "SELECT roomId, name, description FROM room;";
        try {
            connection = MySqlDB.getConnection();
            prepStmt = connection.prepareStatement(sqlQuery);
            resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                roomList.add(Room.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .description(resultSet.getString(3))
                        .build());
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new RuntimeException();
        } finally {
            MySqlDB.sqlClose();
        }
        return roomList;
    }
}
