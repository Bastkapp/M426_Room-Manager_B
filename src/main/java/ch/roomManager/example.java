package ch.roomManager;

import ch.roomManager.dao.DynamicDao;
import ch.roomManager.models.Room;

import java.util.List;

public class example {

  public static void main(String[] args) {
    DynamicDao<Room> roomDao = new DynamicDao<>(Room.class);

    List<Room> roomList = roomDao.getAll();
  }
}
