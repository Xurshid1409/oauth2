package uz.observer;

public interface MySubject<T> {

    void registerObserver(MyObserver<T> observer);
    void unregisterObserver(MyObserver<T> observer);
    void notifyObservers(T event);
}
