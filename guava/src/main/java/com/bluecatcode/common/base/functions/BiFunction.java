package com.bluecatcode.common.base.functions;

import javax.annotation.Nullable;

/**
 * Determines an output value based on an input values.
 *
 * @since 1.1.0
 */
public interface BiFunction<A, B, T> extends CheckedBiFunction<A, B, T, RuntimeException> {

    /**
     * Returns the result of applying this function to {@code first} and {@code second}.
     *
     * @throws NullPointerException if {@code first} or {@code second} is null
     *                              and this function does not accept null arguments
     * @throws RuntimeException if unable to compute
     */
    @Nullable
    T apply(@Nullable A first, @Nullable B second) throws RuntimeException;
}
