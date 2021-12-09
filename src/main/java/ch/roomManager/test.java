package ch.roomManager;

import ch.roomManager.dao.RoomDAO;
import ch.roomManager.models.Room;

import java.util.List;

public class test {

    public static void main(String[] args) {
        List<Room> roomList = new RoomDAO().getAll();
        roomList.forEach(Room::printString);
    }
}
