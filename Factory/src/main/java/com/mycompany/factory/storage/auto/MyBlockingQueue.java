package com.mycompany.factory.storage.auto;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class MyBlockingQueue<E> {
    private final Queue<E> queue;
    private final int maxSize;

    public MyBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
    }

    public synchronized void put(E element) throws InterruptedException {
        while (queue.size() == maxSize) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized E take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        E element = queue.poll();
        notifyAll();
        return element;
    }

    public synchronized boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;

        while (queue.size() == maxSize) {
            if (nanos <= 0) {
                return false;
            }
            TimeUnit.NANOSECONDS.timedWait(this, nanos);
            nanos = deadline - System.nanoTime();
        }

        queue.add(element);
        notifyAll();
        return true;
    }

    public synchronized E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;

        while (queue.isEmpty()) {
            if (nanos <= 0) {
                return null;
            }
            TimeUnit.NANOSECONDS.timedWait(this, nanos);
            nanos = deadline - System.nanoTime();
        }

        E element = queue.poll();
        notifyAll();
        return element;
    }

    public synchronized int remainingCapacity() {
        return maxSize - queue.size();
    }

    public synchronized int size() {
        return queue.size();
    }
}
