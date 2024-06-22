package com.mycompany.factory.dealer;

import lombok.RequiredArgsConstructor;
import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.product.Auto;
import com.mycompany.factory.storage.auto.AutoStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
public class AutoDealer implements Dealer, Runnable, Observable {

    private final AutoStorage autoStorage;
    private Observer observer;
    private int speed = 0;
    private final int id;

    private boolean isRunning = true;

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Auto auto = autoStorage.getAuto();
                if (auto == null)
                    continue;
                Date dateNow = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss a");
                System.out.println(
                        formatForDateNow.format(dateNow) + ": Dealer " + id + ": Auto " + auto.getId() +
                                " (Body: " + auto.getBodyId() + ", Motor: " + auto.getMotorId() + ", Accessory: " + auto.getAccessoryId() + ")");
                notifyObservers(Changes.AUTO_SEND);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void notifyObservers(Changes change) {
        observer.update(Changes.AUTO_SEND);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void changeSpeed(int newSpeed) {
        this.speed = newSpeed;
    }
}
