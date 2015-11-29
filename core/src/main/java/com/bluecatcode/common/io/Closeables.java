package com.bluecatcode.common.io;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Statement;

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

    /**
     * Provides {@link Closeable} interface for {@link Connection}
     * @param connection the connection to decorate
     * @return a closeable decorated connection
     */
    public static Closeable closeableFrom(@Nullable final Connection connection) {
        return closeableFrom(connection, Connection::close);
    }

    /**
     * Provides {@link Closeable} interface for {@link Statement}
     * @param statement the statement to decorate
     * @return a closeable decorated statement
     */
    public static Closeable closeableFrom(@Nullable final Statement statement) {
        return closeableFrom(statement, Statement::close);
    }

    /**
     * Provides {@link Closeable} interface for {@link Clob}
     * @param clob the clob to decorate
     * @return a closeable decorated clob
     */
    public static Closeable closeableFrom(@Nullable final Clob clob) {
        return closeableFrom(clob, Clob::free);
    }

    /**
     * Provides {@link Closeable} interface for {@link T}
     * @param reference the object reference to decorate
     * @param closer the closer function
     * @return a closeable decorated clob
     */
    public static <T> CloseableReference<T> closeableFrom(@Nullable final T reference, Closer<T> closer) {
        return new CloseableReference<>(reference, closer);
    }

    private Closeables() {
        throw new UnsupportedOperationException("Private constructor");
    }

}
