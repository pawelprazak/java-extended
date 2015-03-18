package com.bluecatcode.junit.rules;

import org.junit.rules.ExternalResource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public abstract class SynchronisedExternalResource<T> extends ExternalResource {

    private static final Logger log = Logger.getLogger(SynchronisedExternalResource.class.getName());

    private static final int LOCK_TRY_TIMEOUT = 10;

    private static final ReentrantLock lock = new ReentrantLock(true);

    private final T resource;

    protected SynchronisedExternalResource(T resource) {
        this.resource = resource;
    }

    protected abstract void doBefore() throws Exception;

    protected abstract void doAfter() throws Exception;

    public T getResource() {
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, format("thread %s calls getResource(), %s", Thread.currentThread().getId(), lock));
        }
        if (!lock.isHeldByCurrentThread()) {
            log.log(Level.WARNING, format("Invoked by thread not holding the lock, %s", lock));
        }
        return resource;
    }

    @Override
    protected final synchronized void before() throws Throwable {
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, format("thread %s enters before(), %s", Thread.currentThread().getId(), lock));
        }
        try {
            /* Try to get a lock for n seconds */
            if (lock.tryLock(LOCK_TRY_TIMEOUT, TimeUnit.SECONDS)) {
                try {
                    doBefore();
                } catch (Exception e) {
                    /* at this point we assume something went catastrophically wrong
                     * and we make sure to unlock if possible */
                    unlockIfHeldByCurrentThread();
                    throw new RuntimeException(e);
                }
            } else {
                String message = format("Couldn't get a lock on the database for %s seconds, %s", LOCK_TRY_TIMEOUT, lock);
                log.log(Level.SEVERE, message);
                throw new IllegalStateException(message);
            }
        } catch (InterruptedException e) {
            unlockIfHeldByCurrentThread();
            throw new IllegalStateException(
                    format("Current thread was interrupted while acquiring the lock, %s", lock)
            );
        }

        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, format("thread %s exits before(), %s", Thread.currentThread().getId(), lock));
        }
    }

    @Override
    protected final synchronized void after() {
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, format("thread %s enters after(), %s", Thread.currentThread().getId(), lock));
        }
        try {
            doAfter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            /* always unlock in the end */
            unlockIfHeldByCurrentThread();
        }

        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, format("thread %s exits after(), %s", Thread.currentThread().getId(), lock));
        }
    }

    private static void unlockIfHeldByCurrentThread() {
        /*
            Avoid java.lang.IllegalMonitorStateException
         */
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        } else {
            log.log(Level.SEVERE, format("Cannot unlock as it's not held by the current thread, %s", lock));
        }
    }

}
