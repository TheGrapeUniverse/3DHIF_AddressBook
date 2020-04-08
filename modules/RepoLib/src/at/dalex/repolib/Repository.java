package at.dalex.repolib;

import java.util.Iterator;

public interface Repository<T> {

    boolean insert(T modelToAdd);
    boolean update(T modelToUpdate);
    boolean delete(int id);

    Iterator<T> getAll();
    T getById(int id);
    T getByIndex(int index);
    int getMaxId();

    T createModel();
}
