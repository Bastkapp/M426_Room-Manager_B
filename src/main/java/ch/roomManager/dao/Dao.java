package ch.roomManager.dao;

import ch.roomManager.models.Room;

import java.util.List;

public interface Dao<T, K> {

    default List<T> getAll() {
        throw new UnsupportedOperationException();
    }

    default T getEntity(K k) {
        throw new UnsupportedOperationException();
    }

    default void save (T t) {
        throw new UnsupportedOperationException();
    }

    default void delete (K k) {
        throw new UnsupportedOperationException();
    }

    default Integer count() {
        return 0;
    }

}
