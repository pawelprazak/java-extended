package com.bluecatcode.common.functions;

/**
 * @see Block
 * @see Consumer
 * @see Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 */
public interface CheckedEffect<E extends Exception> {

    /**
     * Performs this operation for side effect.
     *
     * @throws E if unable to compute
     */
    void cause() throws E;
}