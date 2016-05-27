package com.bluecatcode.common.functions;

import javax.annotation.Nullable;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * Unlike most other functional interfaces, Consumer is expected to operate via side-effects.
 *
 * @see <a href="http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">Interface Consumer</a>
 *
 * @see Block
 * @see Effect
 * @see Function
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 * @param <T> the input type of method {@code accept}
 */
public interface Consumer<T> extends CheckedConsumer<T, RuntimeException> {

    /**
     * Performs this operation on the given argument.
     * @param input the input to consume
     * @throws NullPointerException if {@code input} is null and this function does not accept null arguments
     * @throws RuntimeException if unable to compute
     */
    void accept(@Nullable T input) throws RuntimeException;
}
