package com.bluecatcode.common.base;

/**
 * @see java.util.concurrent.Callable
 * @see com.bluecatcode.common.base.Consumer
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 */
public interface Effect {

    /**
     * Performs this operation for side effect.
     *
     * @throws Exception if unable to compute
     */
    void cause() throws Exception;
}