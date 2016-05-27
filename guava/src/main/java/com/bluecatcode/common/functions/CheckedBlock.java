package com.bluecatcode.common.functions;

import javax.annotation.Nullable;

/**
 * @param <T> the result type
 * @see Consumer
 * @see Effect
 * @see Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 */
public interface CheckedBlock<T, E extends Exception> {

    /**
     * Performs this operation returning value.
     *
     * @throws E if unable to compute
     */
    @Nullable
    T execute() throws E;
}
