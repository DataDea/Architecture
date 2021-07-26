package com.easy.concuurent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author yanghai
 * @since 2021/7/22 22:41
 */
public interface ArchitectureFuture<T> extends Future<T> {

    T getUninterruptedly();

    T getUninterruptedly(long timeout, TimeUnit unit);

    ArchitectureFuture<T> awaitUninterruptedly();

    ArchitectureFuture<T> awaitUninterruptedly(long timeout, TimeUnit unit);

    default ArchitectureFuture<T> complete(T result) {
        return complete(result, null);
    }

    ArchitectureFuture<T> complete(T result, Throwable cause);
}
