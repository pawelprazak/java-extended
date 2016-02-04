package com.bluecatcode.common.concurrent;

import com.google.common.base.Optional;

import javax.annotation.WillCloseWhenClosed;
import javax.annotation.WillNotClose;
import java.util.concurrent.TimeUnit;

/**
 * Specialised sleep methods
 *
 * @see TimeUnit
 */
public class Sleep {

    private Sleep() {
        throw new UnsupportedOperationException();
    }

    /**
     * Interruptible sleep of the current thread
     *
     * <b>Will interrupt the thread if InterruptedException occurs</b>
     *
     * @param timeout with unit, the maximum length of time that callers are willing to wait
     * @param unit with timeout, the maximum length of time that callers are willing to wait
     *
     * @see TimeUnit#sleep(long)
     */
    @WillCloseWhenClosed
    public static void sleepFor(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Uninterruptible sleep of the current thread
     *
     * <b>WARNING: you must propagate the exception or interrupt the current thread</b>
     *
     * @param timeout with unit, the maximum length of time that callers are willing to wait
     * @param unit with timeout, the maximum length of time that callers are willing to wait
     * @return true if was interrupted or false if not
     *
     * @see TimeUnit#sleep(long)
     */
    @WillNotClose
    public static Optional<InterruptedException> uninterruptibleSleepFor(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
            return Optional.absent();
        } catch (InterruptedException e) {
            return Optional.of(e);
        }
    }
}
