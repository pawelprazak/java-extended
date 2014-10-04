package com.bluecatcode.common.time;

import java.math.BigDecimal;

import static java.util.concurrent.TimeUnit.*;

public class Milliseconds {

    public static long days(long days) {
        return DAYS.toMillis(days);
    }

    public static long hours(long hours) {
        return HOURS.toMillis(hours);
    }

    public static long minutes(long minutes) {
        return MINUTES.toMillis(minutes);
    }

    public static long seconds(long seconds) {
        return SECONDS.toMillis(seconds);
    }

    /**
     * @throws ArithmeticException if {@code minutes} will not fit in an {@code int}.
     */
    public static int minutes(int minutes) {
        return new BigDecimal(MINUTES.toMillis(minutes)).intValueExact();
    }

    /**
     * @throws ArithmeticException if {@code minutes} will not fit in an {@code int}.
     */
    public static int seconds(int seconds) {
        return new BigDecimal(SECONDS.toMillis(seconds)).intValueExact();
    }

    private Milliseconds() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}
