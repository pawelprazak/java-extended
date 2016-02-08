package com.bluecatcode.common.io;

import javax.annotation.WillClose;

/**
 * @see Closeables#closeableFrom(Object, Closer)
 */
public interface Closer<T> {
    void close(@WillClose T reference) throws Exception;
}
