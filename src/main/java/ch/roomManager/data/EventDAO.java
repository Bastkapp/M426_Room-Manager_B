package ch.roomManager.data;

import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDAO implements Dao<Event, String>{

    @Override
    public List<Event> getAll() {

        List<Event> eventList = new ArrayList<>();
        try {
            String sqlQuery = "";
            ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
            while (resultSet.next()) {
                Event event = new Event();
                setValues(resultSet, event);
                eventList.add(event);
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
    public Event getEntity(String eventUUID) {
        Event event = new Event();

        String sqlQuery =
                "";
        Map<Integer, String> values = new HashMap<>();
        values.put(1, eventUUID);
        try {
            ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
            if (resultSet.next()) {
                setValues(resultSet, event);
            }

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            throw new RuntimeException();
        } finally {
            MySqlDB.sqlClose();
        }
        return event;

    }

    public Integer count() {


        ResultSet resultSet = null;
        String sqlQuery;
        int eventCount = 0;
        try {
            sqlQuery =
                    "SELECT COUNT(eventUUID)" +
                            " FROM Event";

            resultSet = MySqlDB.sqlSelect(sqlQuery);
            if (resultSet.next()) {
                eventCount = resultSet.getInt(1);
            }
        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            throw new RuntimeException();


        } finally {
            return eventCount;
        }
    }

    private void setValues(ResultSet resultSet, Event event)
            throws SQLException{
        event.setId(resultSet.getInt("eventUUID"));
        event.setId(resultSet.getInt("event"));
    }
}
