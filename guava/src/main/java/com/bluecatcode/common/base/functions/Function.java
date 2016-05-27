package com.bluecatcode.common.base.functions;

import javax.annotation.Nullable;

/**
 * Determines an output value based on an input value.
 *
 * @since 1.1.0
 */
public interface Function<F, T> extends CheckedFunction<F, T, RuntimeException> {
  /**
   * Returns the result of applying this function to {@code input}. This method is <i>generally
   * expected</i>, but not absolutely required, to have the following properties:
   *
   * <ul>
   * <li>Its execution does not cause any observable side effects.
   * <li>The computation is <i>consistent with equals</i>
   * </ul>
   *
   * @throws NullPointerException if {@code input} is null and this function does not accept null arguments
   * @throws RuntimeException if unable to compute
   */
  @Nullable
  T apply(@Nullable F input) throws RuntimeException;
}
