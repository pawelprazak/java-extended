package com.bluecatcode.common.base;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.bluecatcode.common.base.FunctionalInterfaces.callable;

public class Try {

    public <T> T tryWith(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void tryWith(Effect effect) {
        tryWith(callable(effect));
    }

    public void tryWith(long timeoutDuration, TimeUnit timeoutUnit, Effect effect) {
        tryWith(timeoutDuration, timeoutUnit, callable(effect));
    }

    public <T> T tryWith(long timeoutDuration, TimeUnit timeoutUnit, Callable<T> callable) {
        try {
            TimeLimiter limiter = new SimpleTimeLimiter();
            //noinspection unchecked
            Callable<T> proxy = limiter.newProxy(callable, Callable.class, timeoutDuration, timeoutUnit);
            return proxy.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
