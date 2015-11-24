package com.bluecatcode.common.io;

/**
 * @see Closeables#closeableFrom(Object, Closer)
 */
public interface Closer<T> {
    void close(T reference) throws Exception;
}
