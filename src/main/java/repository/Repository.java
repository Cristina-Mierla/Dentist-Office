package repository;

import java.util.Collection;

public interface Repository<T,Tid> {
    void add(T obj);
    void delete(T obj);
    void update(T obj, Tid id);
    T findById (Tid id);
    Iterable<T> getAll();
    Collection<T> getCollection();
    String toString();
}
