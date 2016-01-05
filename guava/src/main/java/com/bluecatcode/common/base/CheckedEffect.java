package com.bluecatcode.common.base;

/**
 * @see com.bluecatcode.common.base.Block
 * @see com.bluecatcode.common.base.Consumer
 * @see com.google.common.base.Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 */
public interface CheckedEffect {

    /**
     * Performs this operation for side effect.
     *
     * @throws Exception if unable to compute
     */
    void cause() throws Exception;
}