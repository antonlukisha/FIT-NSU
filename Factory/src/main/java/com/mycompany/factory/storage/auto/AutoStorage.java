package com.mycompany.factory.storage.auto;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Auto;
import com.mycompany.factory.storage.Storage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AutoStorage implements Storage, Observable {

    private MyBlockingQueue<Auto> storage;
    private int size;

    private Observer observer;

    @Override
    public void setSize(int size) {
        this.size = size;
        storage = new MyBlockingQueue<>(size);
    }

    @Override
    public int onStorage() {
        return size - storage.remainingCapacity();
    }

    public boolean addToStorage(Auto product) {
        try {
            boolean result = storage.offer(product, 4000, TimeUnit.MILLISECONDS);
            if (result) notifyObservers(Changes.MOTOR_STORAGE_UPDATE);
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFull() {
        return storage.remainingCapacity() == 0;
    }

    public Auto getAuto() throws InterruptedException {
        Auto auto = storage.poll(4000, TimeUnit.MILLISECONDS);
        notifyObservers(Changes.AUTO_STORAGE_UPDATE);
        return auto;
    }

    @Override
    public void notifyObservers(Changes change) {
        observer.update(change);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }
}
