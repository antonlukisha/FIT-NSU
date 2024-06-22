package com.mycompany.factory.storage;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Body;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BodyStorage implements Storage, Observable {

    private BlockingQueue<Body> storage;
    private int size;

    private Observer observer;

    @Override
    public void setSize(int size) {
        this.size = size;
        storage = new ArrayBlockingQueue<>(size);
    }

    @Override
    public int onStorage() {
        return size - storage.remainingCapacity();
    }

    public boolean addToStorage(Body product) {
        try {
            boolean result = storage.offer(product, 4000, TimeUnit.MILLISECONDS);
            if (result) notifyObservers(Changes.MOTOR_STORAGE_UPDATE);
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Body getBody() throws InterruptedException {
        Body body = storage.poll(4000, TimeUnit.MILLISECONDS);
        notifyObservers(Changes.BODY_STORAGE_UPDATE);
        return body;
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
