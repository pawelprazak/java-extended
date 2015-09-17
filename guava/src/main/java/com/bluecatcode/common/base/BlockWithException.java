package com.bluecatcode.common.base;

/**
 * @see java.util.concurrent.Callable
 * @see Consumer
 * @see Effect
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 */
public interface BlockWithException<T> {

    /**
     * Performs this operation returning value.
     *
     * @throws Exception if unable to compute
     */
    T execute() throws Exception;
}
