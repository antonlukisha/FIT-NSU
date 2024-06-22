package com.mycompany.factory.supplier;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Motor;
import com.mycompany.factory.storage.MotorStorage;

public class MotorSupplier extends BasicSupplier implements Runnable, Observable {

    private final MotorStorage motorStorage;
    protected static int productId = 0;

    private Observer observer;

    private final boolean logging;
    private boolean isRunning = true;

    public MotorSupplier(MotorStorage motorStorage, boolean logging) {
        this.motorStorage = motorStorage;
        this.logging = logging;
        speed = 0;
    }

    public void shutdown() {isRunning = false;}

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                break;
            }
            if (!motorStorage.addToStorage(new Motor(productId)))
                continue;
            if (logging)
                System.out.println("MotorSupplier add motor#" + productId);
            productId++;
            notifyObservers(Changes.MOTOR_PRODUCED);
        }
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
