package com.mycompany.factory.worker;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Accessory;
import com.mycompany.factory.product.Auto;
import com.mycompany.factory.product.Body;
import com.mycompany.factory.product.Motor;
import com.mycompany.factory.storage.AccessoryStorage;
import com.mycompany.factory.storage.BodyStorage;
import com.mycompany.factory.storage.MotorStorage;
import com.mycompany.factory.storage.auto.AutoStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoWorker implements Worker, Runnable, Observable {

    private final BodyStorage bodyStorage;
    private final AccessoryStorage accessoryStorage;
    private final MotorStorage motorStorage;
    private final AutoStorage autoStorage;

    private int productId;
    private static final AtomicInteger lastProductId = new AtomicInteger(-1);
    private final int id;

    private final List<Observer> observers = new ArrayList<>();

    private final boolean logging;
    private boolean isWaiting = false;
    private boolean isRunning = true;

    public AutoWorker(BodyStorage bodyStorage, AccessoryStorage accessoryStorage, MotorStorage motorStorage, AutoStorage autoStorage, int id, boolean logging) {
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.motorStorage = motorStorage;
        this.autoStorage = autoStorage;
        this.id = id;
        this.logging = logging;
        productId = lastProductId.incrementAndGet();
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                isWaiting = false;
                notifyObservers(Changes.START_PRODUCING_AUTO);
                Body body;
                Accessory accessory;
                Motor motor;

                body = bodyStorage.getBody();
                accessory = accessoryStorage.getAccessory();
                motor = motorStorage.getMotor();
                if (body == null || accessory == null || motor == null)
                    continue;

                if (logging)
                    System.out.println("AutoWorker #" + id + " add auto #" + productId);
                productId = lastProductId.incrementAndGet();
                synchronized (this) {
                    isWaiting = true;
                    if (!autoStorage.addToStorage(new Auto(body.getId(), motor.getId(), accessory.getId(), productId)))
                        continue;
                    notifyObservers(Changes.AUTO_PRODUCED);
                    this.wait();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void notifyObservers(Changes change) {
        for (Observer observer : observers)
            observer.update(change);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}