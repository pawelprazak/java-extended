package com.bluecatcode.common.io;

import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Statement;

import static com.bluecatcode.common.contract.Impossibles.impossible;
import static com.google.common.io.Closeables.close;

/**
 * Provides {@link Closeable} and {@link AutoCloseable} interfaces for incompatible resources
 * <p>
 * Enables usage of resources incompatible with Closable interface in contexts
 * where they are required, e.g. Java 7 try-with-resources.
 * </p>
 * Methods are wrapped with decorators and parameters can safely be null.
 * All decorators have non-throwing null-checks, <b>null values are silently ignored</b>.
 * All checked exceptions are translated to IOException and rethrown.
 */
public class Closeables {

    private Closeables() {
        throw new UnsupportedOperationException("Private constructor");
    }

    /**
     * Provides {@link Closeable} interface for {@link Connection}
     *
     * @param connection the connection to decorate
     * @return a closeable decorated connection
     */
    public static Closeable closeableFrom(@WillNotClose @Nullable final Connection connection) {
        return closeableFrom(connection, Connection::close);
    }

    /**
     * Provides {@link Closeable} interface for {@link Statement}
     *
     * @param statement the statement to decorate
     * @return a closeable decorated statement
     */
    public static Closeable closeableFrom(@WillNotClose @Nullable final Statement statement) {
        return closeableFrom(statement, Statement::close);
    }

    /**
     * Provides {@link Closeable} interface for {@link Clob}
     *
     * @param clob the clob to decorate
     * @return a closeable decorated clob
     */
    public static Closeable closeableFrom(@WillNotClose @Nullable final Clob clob) {
        return closeableFrom(clob, Clob::free);
    }

    /**
     * Provides {@link Closeable} interface for {@link Blob}
     *
     * @param blob the blob to decorate
     * @return a closeable decorated clob
     */
    public static Closeable closeableFrom(@WillNotClose @Nullable final Blob blob) {
        return closeableFrom(blob, Blob::free);
    }

    /**
     * Provides {@link Closeable} interface for {@link T}
     *
     * @param reference the object reference to decorate
     * @param closer    the closer function
     * @param <T>       the closeable reference type
     * @return a closeable decorated clob
     */
    public static <T> CloseableReference<T> closeableFrom(@WillNotClose @Nullable final T reference, Closer<T> closer) {
        return new CloseableReference<>(reference, closer);
    }

    public static void closeQuietly(@WillClose @Nullable Closeable closeable) {
        try {
            close(closeable, true);
        } catch (IOException impossible) {
            impossible(impossible);
        }
    }
}
