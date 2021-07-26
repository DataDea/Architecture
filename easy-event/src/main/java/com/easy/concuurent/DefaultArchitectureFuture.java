package com.easy.concuurent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yanghai
 * @since 2021/7/22 22:51
 */
public class DefaultArchitectureFuture<T> implements ArchitectureFuture<T> {

    private final AtomicReference<T> result;
    private final AtomicReference<Throwable> cause;
    private final CountDownLatch latch;
    private final AtomicBoolean completed;
    private final ReentrantLock lock;
    private final boolean safeCompletion;

    public DefaultArchitectureFuture() {
        this(false);
    }

    public DefaultArchitectureFuture(boolean safeCompletion) {
        this.result = new AtomicReference<>();
        this.cause = new AtomicReference<>();
        this.latch = new CountDownLatch(1);
        this.completed = new AtomicBoolean(false);
        this.lock = new ReentrantLock();
        this.safeCompletion = safeCompletion;
    }


    @Override
    public T getUninterruptedly() {
        try {
            return get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public T getUninterruptedly(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ArchitectureFuture<T> awaitUninterruptedly() {
        getUninterruptedly();
        return this;
    }

    @Override
    public ArchitectureFuture<T> awaitUninterruptedly(long timeout, TimeUnit unit) {
        getUninterruptedly(timeout, unit);
        return this;
    }

    @Override
    public ArchitectureFuture<T> complete(T result, Throwable cause) {
        if (!completed.compareAndSet(false, true)) {
            if (safeCompletion) {
                // already completed but safeCompletion is true, return directly
                return this;
            } else {
                throw new IllegalStateException("Current ArchitectureFuture already completed");
            }
        }
        this.result.set(result);
        this.cause.set(cause);
        this.latch.countDown();
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        if (cause.get() != null) {
            throw new ExecutionException(cause.get());
        }
        return result.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean ret = latch.await(timeout, unit);
        if (!ret) {
            throw new TimeoutException();
        }
        if (cause.get() != null) {
            throw new ExecutionException(cause.get());
        }
        return result.get();
    }
}
