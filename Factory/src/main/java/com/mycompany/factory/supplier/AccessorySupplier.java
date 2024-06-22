package com.mycompany.factory.supplier;

import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Accessory;
import com.mycompany.factory.storage.AccessoryStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier extends BasicSupplier implements Runnable, Observable {
    private final int id;
    private final AccessoryStorage accessoriesStorage;
    private int productId;
    private static final AtomicInteger lastProductId = new AtomicInteger(-1);

    private Observer observer;

    private final boolean logging;
    private boolean isRunning = true;

    public AccessorySupplier(int id, AccessoryStorage accessoriesStorage, boolean logging) {
        this.id = id;
        this.accessoriesStorage = accessoriesStorage;
        this.logging = logging;
        this.speed = 0;
        productId = lastProductId.incrementAndGet();
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
            if (!accessoriesStorage.addToStorage(new Accessory(productId)))
                continue;
            if (logging)
                System.out.println("AccessorySupplier #" + id + " add accessory #" + productId);
            productId = lastProductId.incrementAndGet();
            notifyObservers(Changes.ACCESSORY_PRODUCED);
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
