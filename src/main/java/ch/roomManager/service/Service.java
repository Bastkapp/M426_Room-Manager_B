package ch.roomManager.service;

import ch.roomManager.dao.Result;
import ch.roomManager.db.MySqlDB;

public abstract class Service {

    protected int getHttpStatus() {
        if (MySqlDB.getResult() == Result.SUCCESS)
            return 200;
        return 500;
    }
}
