package domain;

public interface Identifiable<T> {
    T getID();
    void setID(T id);
}
