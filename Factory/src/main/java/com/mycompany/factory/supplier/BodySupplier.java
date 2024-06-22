package com.mycompany.factory.supplier;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Body;
import com.mycompany.factory.storage.BodyStorage;

public class BodySupplier extends BasicSupplier implements Runnable, Observable {

    private final BodyStorage bodyStorage;
    protected static int productId = 0;

    private Observer observer;

    private final boolean logging;
    private boolean isRunning = true;

    public BodySupplier(BodyStorage bodyStorage, boolean logging) {
        this.bodyStorage = bodyStorage;
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
            if (!bodyStorage.addToStorage(new Body(productId)))
                continue;
            if (logging)
                System.out.println("BodySupplier add body#" + productId);
            productId++;
            notifyObservers(Changes.BODY_PRODUCED);
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
