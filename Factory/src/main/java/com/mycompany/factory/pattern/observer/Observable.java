package com.mycompany.factory.pattern.observer;

public interface Observable {
    void notifyObservers(Changes change);
    void addObserver(Observer observer);
}
