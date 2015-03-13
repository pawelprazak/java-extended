package com.bluecatcode.common.io;

import com.google.common.annotations.Beta;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

/**
 * An output stream that writes the length of the data at the start.
 * Compatible with ByteArrayOutputStream.
 */
@Beta
public class BoundedByteArrayOutputStream extends ByteArrayOutputStream {

    private int limit;

    public BoundedByteArrayOutputStream(int limit) {
        this(32, limit);
    }

    public BoundedByteArrayOutputStream(int capacity, int limit) {
        super(capacity);
        checkArgument(capacity > 0, format("Invalid capacity: %s > 0", capacity));
        checkArgument(limit > 0, format("Invalid limit: %s > 0", limit));
        this.limit = limit;
    }

    @Override
    public synchronized void write(int b) {
        if (super.size() >= limit) {
            throw new IllegalArgumentException(format("Reached the limit of the buffer (%s >= %s)", super.size(), limit));
        }
        super.write(b);
    }

    @Override
    public synchronized void write(@Nonnull byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    @Override
    public synchronized void write(@Nonnull byte[] bytes, int offset, int length) {
        if ((offset < 0) || (offset > bytes.length)) {
            throw new IndexOutOfBoundsException("Invalid offset: " + offset);
        }
        if ((length < 0) || ((offset + length) > bytes.length) || ((offset + length) < 0)) {
            throw new IndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length == 0) {
            return;
        }

        int newSize = super.size() + length;
        if (newSize > limit) {
            throw new IllegalArgumentException(format("Reached the buffer limit (%s > %s)", newSize, limit));
        }
        super.write(bytes, offset, length);
    }

}