package com.bluecatcode.common.io;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

public class CloseableReference<T> implements Closeable {

    private final T reference;
    private final Closer<T> closer;

    public CloseableReference(@Nullable T reference, Closer<T> closer) {
        this.reference = reference;
        if (closer == null) {
            throw new IllegalArgumentException("Expected closer to be non-null");
        }
        this.closer = closer;
    }

    public T get() {
        return reference;
    }

    @Override
    public void close() throws IOException {
        try {
            if (reference != null) {
                closer.close(reference);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
