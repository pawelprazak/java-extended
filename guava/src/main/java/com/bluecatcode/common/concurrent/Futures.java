package com.bluecatcode.common.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.immediateFailedFuture;

/**
 * @see com.google.common.util.concurrent.Futures
 */
public class Futures {

    /**
     * Function submits the task with given executor and callback
     * @param executor the executor service to use
     * @param task the task to submit
     * @param callback the callback to call on completion
     * @param <T> the function input type
     * @param <R> the function future return type
     * @return future result of the <tt>task</tt>
     * @throws IllegalArgumentException if any of the arguments is null
     */
    public static <T, R> Function<T, ListenableFuture<R>> futureWith(final ListeningExecutorService executor,
                                                                     final Function<T, R> task,
                                                                     final FutureCallback<R> callback) {
        checkArgument(executor != null, "Expected non-null executor");
        checkArgument(task != null, "Expected non-null task");
        checkArgument(callback != null, "Expected non-null callback");

        return input -> {
            try {
                ListenableFuture<R> future = executor.submit(() -> task.apply(input));
                addCallback(future, callback, executor);
                return future;
            } catch (Throwable t) {
                callback.onFailure(t);
                return immediateFailedFuture(t);
            }
        };
    }
}
