package ch.roomManager.data;

import java.util.List;

public interface Dao<T, K> {

    default List<T> getAll() {
        throw new UnsupportedOperationException();
    }


    default T getEntity(K k) {
        throw new UnsupportedOperationException();
    }
}
