package com.bluecatcode.common.base;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * Unlike most other functional interfaces, Consumer is expected to operate via side-effects.
 *
 * @see <a href="http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">Interface Consumer</a>
 */
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     * @param input the input to consume
     */
    void accept(T input);
}
