package com.bluecatcode.common.functions;

/**
 * @see Block
 * @see Consumer
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 */
public interface Effect extends CheckedEffect<RuntimeException> {

    /**
     * Performs this operation for side effect.
     *
     * @throws RuntimeException if unable to compute
     */
    void cause() throws RuntimeException;
}