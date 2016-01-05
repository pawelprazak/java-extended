package com.bluecatcode.junit.rules;

import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * @see org.junit.rules.Timeout
 * @since 1.0.4
 */
public class Timeouts {

    /**
     * Create a {@code Timeout} instance with the timeout specified
     * at the timeUnit of granularity of the provided {@code TimeUnit}.
     *
     * @param timeout the maximum time to allow the test to run
     * before it should timeout
     * @param timeUnit the time unit for the {@code timeout}
     */
    public static Timeout timeout(long timeout, TimeUnit timeUnit) {
        /* use @Deprecated constructor for compatibility with versions before 4.12 */
        //noinspection deprecation
        return new Timeout((int) timeUnit.toMillis(timeout));
    }
}
