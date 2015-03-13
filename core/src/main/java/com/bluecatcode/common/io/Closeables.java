package com.bluecatcode.common.io;

import com.bluecatcode.common.annotations.Beta;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides {@link Closeable} and {@link AutoCloseable} interfaces for incompatible resources
 * <p/>
 * Enables usage of resources incompatible with Closable interface in contexts
 * where they are required, e.g. Java 7 try-with-resources or {@link com.google.common.io.Closer#register}.
 * <p/>
 * Methods are wrapped with decorators and parameters can safely be null.
 * All decorators have non-throwing null-checks, <b>null values are silently ignored</b>.
 * All checked exceptions are translated to IOException and rethrown.
 */
@Beta
public class Closeables {

    /**
     * Provides {@link Closeable} interface for {@link Connection}
     * @param connection the connection to decorate
     * @return a closeable decorated connection
     * @throws java.io.IOException with a wrapped checked exception
     */
    public static Closeable closeableFrom(@Nullable final Connection connection) {
        return new Closeable() {
            @Override
            public void close() throws IOException {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        };
    }

    /**
     * Provides {@link Closeable} interface for {@link Statement}
     * @param statement the statement to decorate
     * @return a closeable decorated statement
     * @throws java.io.IOException with a wrapped checked exception
     */
    public static Closeable closeableFrom(@Nullable final Statement statement) {
        return new Closeable() {
            @Override
            public void close() throws IOException {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        };
    }

    /**
     * Provides {@link Closeable} interface for {@link Clob}
     * @param clob the clob to decorate
     * @return a closeable decorated clob
     * @throws java.io.IOException with a wrapped checked exception
     */
    public static Closeable closeableFrom(@Nullable final Clob clob) {
        return new Closeable() {
            @Override
            public void close() throws IOException {
                try {
                    if (clob != null) {
                        clob.free();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        };
    }

    private Closeables() {
        throw new UnsupportedOperationException("Private constructor");
    }
}
