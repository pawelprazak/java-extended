package com.bluecatcode.common.base;

/**
 * @see java.util.concurrent.Callable
 * @see com.bluecatcode.common.base.Consumer
 * @see com.bluecatcode.common.base.Effect
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 */
public interface Block<T> {

    /**
     * Performs this operation returning value.
     *
     * @throws RuntimeException if unable to compute
     */
    T execute();
}
