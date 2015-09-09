package com.bluecatcode.time;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * Converts from a time unit to the milliseconds
 * @see TimeUnit#toMillis
 */
public class Milliseconds {

    /**
     * Convert days to milliseconds
     * @param days the minutes
     * @return the milliseconds
     */
    public static long days(long days) {
        return DAYS.toMillis(days);
    }

    /**
     * Convert hours to milliseconds
     * @param hours the minutes
     * @return the milliseconds
     */
    public static long hours(long hours) {
        return HOURS.toMillis(hours);
    }

    /**
     * Convert minutes to milliseconds
     * @param minutes the minutes
     * @return the milliseconds
     */
    public static long minutes(long minutes) {
        return MINUTES.toMillis(minutes);
    }

    /**
     * Convert seconds to milliseconds
     * @param seconds the seconds
     * @return the milliseconds
     */
    public static long seconds(long seconds) {
        return SECONDS.toMillis(seconds);
    }

    /**
     * Convert minutes to milliseconds
     * @param minutes the minutes
     * @return the milliseconds
     * @throws ArithmeticException if {@code minutes} will not fit in an {@code int}.
     */
    public static int minutes(int minutes) {
        return new BigDecimal(MINUTES.toMillis(minutes)).intValueExact();
    }

    /**
     * Convert seconds to milliseconds
     * @param seconds the seconds
     * @return the milliseconds
     * @throws ArithmeticException if {@code seconds} will not fit in an {@code int}.
     */
    public static int seconds(int seconds) {
        return new BigDecimal(SECONDS.toMillis(seconds)).intValueExact();
    }

    private Milliseconds() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}
