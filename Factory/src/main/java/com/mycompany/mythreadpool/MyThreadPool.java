package com.mycompany.mythreadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool implements ExecutorService {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final Condition condition = reentrantLock.newCondition();

    private final BlockingQueue<Runnable> taskQueue;
    private final int maxThreads;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final AtomicInteger tasksCounter = new AtomicInteger(0);

    private final List<WorkerThread> tasks = new ArrayList<>();

    public MyThreadPool(int maxThreads) {
        this(new ArrayBlockingQueue<>(maxThreads), maxThreads);
    }

    public MyThreadPool(BlockingQueue<Runnable> taskQueue, int maxThreads) {
        this.maxThreads = maxThreads;
        this.taskQueue = taskQueue;
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        reentrantLock.lock();
        try {
            taskQueue.put(command);
            if (tasksCounter.get() < maxThreads) {
                addNewThread();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void execute() {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        reentrantLock.lock();
        try {
            while (!taskQueue.isEmpty() || tasksCounter.get() < maxThreads) {
                addNewThread();
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    private void addNewThread() {
        WorkerThread thread = new WorkerThread();
        tasks.add(thread);
        thread.start();
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            tasksCounter.incrementAndGet();
            try {
                while (!isShutdown.get() && !Thread.currentThread().isInterrupted()) {
                    Runnable currentTask = taskQueue.poll();
                    if (currentTask == null)
                        break;
                    currentTask.run();
                }
            } finally {
                threadEnd(this);
            }
        }
    }

    private void threadEnd(WorkerThread workerThread) {
        reentrantLock.lock();
        try {
            tasksCounter.decrementAndGet();
            tasks.remove(workerThread);
            condition.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }


    @Override
    public void shutdown() {
        isShutdown.set(true);
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown.set(true);
        reentrantLock.lock();
        try {
            if (!isTerminated()) {
                for (WorkerThread workerThread : tasks) {
                    workerThread.interrupt();
                }
            }
        } finally {
            reentrantLock.unlock();
        }
        List<Runnable> remainingTasks = new ArrayList<>();
        taskQueue.drainTo(remainingTasks);
        return remainingTasks;
    }

    @Override
    public boolean isShutdown() {
        return isShutdown.get();
    }

    @Override
    public boolean isTerminated() {
        return isShutdown.get() && tasks.isEmpty();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        reentrantLock.lock();
        try {
            while (!tasks.isEmpty() && (endTime - System.currentTimeMillis() > 0)) {
                condition.await(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }
        } finally {
            reentrantLock.unlock();
        }
        if (tasks.isEmpty())
            shutdown();
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        FutureTask<T> futureTask = new FutureTask<>(task, result);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public Future<?> submit(Runnable task) {
        FutureTask<?> futureTask = new FutureTask<>(task, null);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            futures.add(submit(task));
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        long startTime = System.currentTimeMillis();
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            long timeLeft = startTime + unit.toMillis(timeout) - System.currentTimeMillis();
            if (timeLeft <= 0) {
                return futures;
            }
            futures.add(submit(task));
        }
        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        List<Future<T>> futures = invokeAll(tasks);
        for (Future<T> future : futures) {
            try {
                return future.get();
            } catch (ExecutionException e) {
                //
            }
        }
        throw new ExecutionException("All tasks failed", null);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long startTime = System.currentTimeMillis();
        List<Future<T>> futures = invokeAll(tasks, timeout, unit);
        long remainingTime = startTime + unit.toMillis(timeout) - System.currentTimeMillis();
        for (Future<T> future : futures) {
            try {
                return future.get(remainingTime, TimeUnit.MILLISECONDS);
            } catch (ExecutionException e) {
                //
            } catch (TimeoutException e) {
                remainingTime = startTime + unit.toMillis(timeout) - System.currentTimeMillis();
                if (remainingTime <= 0) {
                    throw new TimeoutException("Timed out waiting for any task to complete");
                }
            }
        }
        throw new ExecutionException("All tasks failed", null);
    }
}
