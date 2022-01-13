package ch.roomManager.data;

import ch.roomManager.db.MySqlDB;
import ch.roomManager.models.Reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAO implements Dao<Reservation, String>{

    @Override
    public List<Reservation> getAll() {

        List<Reservation> reservationList = new ArrayList<>();
        try {
            String sqlQuery = "";
            ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                setValues(resultSet, reservation);
                reservationList.add(reservation);
            }

        } catch (SQLException sqlEx) {
            MySqlDB.printSQLException(sqlEx);
            throw new RuntimeException();
        } finally {
            MySqlDB.sqlClose();
        }
        return reservationList;
    }


    @Override
    public Reservation getEntity(String reservationUUID) {
        Reservation reservation = new Reservation();

        String sqlQuery =
                "";
        Map<Integer, String> values = new HashMap<>();
        values.put(1, reservationUUID);
        try {
            ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
            if (resultSet.next()) {
                setValues(resultSet, reservation);
            }

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            throw new RuntimeException();
        } finally {
            MySqlDB.sqlClose();
        }
        return reservation;

    }

    public Integer count() {


        ResultSet resultSet = null;
        String sqlQuery;
        int reservationCount = 0;
        try {
            sqlQuery =
                    "SELECT COUNT(reservationUUID)" +
                            " FROM Reservation";

            resultSet = MySqlDB.sqlSelect(sqlQuery);
            if (resultSet.next()) {
                reservationCount = resultSet.getInt(1);
            }
        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            throw new RuntimeException();


        } finally {
            return reservationCount;
        }
    }

    private void setValues(ResultSet resultSet, Reservation reservation)
            throws SQLException{
        reservation.setId(resultSet.getInt("reservationUUID"));
        reservation.setId(resultSet.getInt("reservation"));
    }
}
