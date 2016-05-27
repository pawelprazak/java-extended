package com.bluecatcode.common.functions;

import javax.annotation.Nullable;

/**
 * @see java.util.concurrent.Callable
 * @see Consumer
 * @see Effect
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 */
public interface Block<T> extends CheckedBlock<T, RuntimeException> {

    /**
     * Performs this operation returning value.
     *
     * @throws RuntimeException if unable to compute
     */
    @Nullable
    T execute() throws RuntimeException;
}
