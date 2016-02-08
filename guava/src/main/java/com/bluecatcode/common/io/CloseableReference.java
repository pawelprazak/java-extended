package com.bluecatcode.common.io;

import javax.annotation.Nullable;
import javax.annotation.WillCloseWhenClosed;
import java.io.Closeable;
import java.io.IOException;

import static com.bluecatcode.common.base.Predicates.isNotNull;
import static com.bluecatcode.common.contract.Checks.check;
import static java.lang.String.format;

/**
 * A {@link Closeable} wrapper for any reference
 *
 * @param <T> the reference type
 */
public class CloseableReference<T> implements Closeable {

    private final T reference;
    private final Closer<T> closer;
    private boolean wasClosed;

    /**
     * @param reference the reference to wrap
     * @param closer    the function to use to close the reference
     */
    public CloseableReference(@WillCloseWhenClosed @Nullable T reference, Closer<T> closer) {
        this.reference = check(reference, isNotNull(), "Expected non-null reference");
        this.closer = check(closer, isNotNull(), "Expected non-null closer");
        this.wasClosed = false;
    }

    /**
     * Get the reference if available
     *
     * @return the reference
     * @throws IllegalStateException if the reference is closed
     */
    public T get() {
        check(!wasClosed, new IllegalStateException("The reference is closed"));
        return reference;
    }

    @Override
    public String toString() {
        return format("CloseableReference.of(%s)", reference);
    }

    /**
     * Close (or free) the reference
     *
     * @throws IOException if any exception occurs
     */
    @Override
    public void close() throws IOException {
        try {
            if (reference != null) {
                closer.close(reference);
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            this.wasClosed = true;
        }
    }
}
