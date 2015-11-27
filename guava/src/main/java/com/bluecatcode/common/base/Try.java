package com.bluecatcode.common.base;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Try {

    public static <T> T tryWith(Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e; // no need to wrap
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void tryWith(Effect effect) {
        try {
            effect.cause();
        } catch (RuntimeException e) {
            throw e; // no need to wrap
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T tryWith(long timeoutDuration, TimeUnit timeoutUnit, Callable<T> callable) {
        try {
            TimeLimiter limiter = new SimpleTimeLimiter();
            //noinspection unchecked
            Callable<T> proxy = limiter.newProxy(callable, Callable.class, timeoutDuration, timeoutUnit);
            return proxy.call();
        } catch (RuntimeException e) {
            throw e; // no need to wrap
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void tryWith(long timeoutDuration, TimeUnit timeoutUnit, Effect effect) {
        try {
            TimeLimiter limiter = new SimpleTimeLimiter();
            //noinspection unchecked
            Effect proxy = limiter.newProxy(effect, Effect.class, timeoutDuration, timeoutUnit);
            proxy.cause();
        } catch (RuntimeException e) {
            throw e; // no need to wrap
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
