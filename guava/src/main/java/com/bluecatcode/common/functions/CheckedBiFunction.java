package com.bluecatcode.common.functions;

import javax.annotation.Nullable;

/**
 * Determines an output value based on an input values.
 *
 * @since 1.0.5
 */
public interface CheckedBiFunction<A, B, T, E extends Exception> {

    /**
     * Returns the result of applying this function to {@code first} and {@code second}.
     *
     * @throws NullPointerException if {@code first} or {@code second} is null
     *                              and this function does not accept null arguments
     * @throws E if unable to compute
     */
    @Nullable
    T apply(@Nullable A first, @Nullable B second) throws E;
}
