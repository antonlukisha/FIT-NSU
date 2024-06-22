package com.mycompany.factory.storage;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Accessory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AccessoryStorage implements Storage, Observable {
    private BlockingQueue<Accessory> storage;
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

    public boolean addToStorage(Accessory product) {
        try {
            boolean result = storage.offer(product, 4000, TimeUnit.MILLISECONDS);
            if (result) notifyObservers(Changes.MOTOR_STORAGE_UPDATE);
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Accessory getAccessory() throws InterruptedException {
        Accessory accessory = storage.poll(4000, TimeUnit.MILLISECONDS);
        notifyObservers(Changes.ACCESSORY_STORAGE_UPDATE);
        return accessory;
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
