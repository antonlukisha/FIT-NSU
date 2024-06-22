package com.mycompany.factory.storage.auto;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observer;
import com.mycompany.factory.worker.AutoWorker;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AutoStorageController implements Observer {

    private final AutoStorage autoStorage;
    @Setter
    private List<AutoWorker> autoWorkers = new ArrayList<>();

    @Override
    public void update(Changes change) {
        if (change == Changes.AUTO_SEND) {
            if (!autoStorage.isFull()) {
                for (AutoWorker autoWorker : autoWorkers) {
                    if (autoWorker.isWaiting()) {
                        synchronized (autoWorker) {
                            autoWorker.notify();
                        }
                    }
                }
            }
        }
    }
}
