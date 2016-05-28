package com.bluecatcode.common.concurrent;

import com.bluecatcode.common.base.Either;
import com.bluecatcode.common.exceptions.VoidException;
import com.bluecatcode.common.exceptions.WrappedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static com.bluecatcode.common.concurrent.Try.with;
import static com.bluecatcode.common.concurrent.TryTest.TransactionalDataSource.using;
import static org.mockito.Mockito.mock;

public class TryTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldNotThrow() throws IOException, SQLException {
        DataSource dataSource = mock(DataSource.class);

        using(dataSource).with(
                connection -> {
                    if (1 == 2) {
                        throw new IOException("test");
                    }
                    if (1 == 3) {
                        throw new SQLException("test");
                    }
                });
    }

    @Test
    public void shouldThrowIOException() throws IOException, SQLException {
        DataSource dataSource = mock(DataSource.class);

        exception.expect(IOException.class);

        using(dataSource).with(
                connection -> {
                    if (1 == 1) {
                        throw new IOException("test");
                    }
                    if (1 == 2) {
                        throw new SQLException("test");
                    }
                });
    }

    @Test
    public void shouldThrowSQLException() throws IOException, SQLException {
        DataSource dataSource = mock(DataSource.class);

        exception.expect(SQLException.class);

        using(dataSource).with(
                connection -> {
                    if (1 == 2) {
                        throw new IOException("test");
                    }
                    if (1 == 1) {
                        throw new SQLException("test");
                    }
                });
    }

    @Test
    public void testName() {
        Closeable closeable = () -> {
        };

        String result = with(closeable, String.class)
                .rethrow(RuntimeException.class)
                .limit(1, TimeUnit.MINUTES)
                .tryA(c -> "test");

        Either<WrappedException, String> either = with(closeable, String.class)
                .rethrow(RuntimeException.class)
                .limit(1, TimeUnit.MINUTES)
                .either(c -> "test");

        try {
            with(closeable, String.class)
                    .rethrow(IOException.class)
                    .limit(1, TimeUnit.MINUTES)
                    .tryA(c -> {
                        throw new IOException();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        with(closeable, String.class)
                .swallow()
                .limit(1, TimeUnit.MINUTES)
                .tryA(c -> {
                    throw new IOException();
                });

        exception.expect(WrappedException.class);

        try {
            with(closeable, String.class)
                    .wrap()
//                    .limit(1, TimeUnit.MINUTES)
                    .tryA(c -> {
                        throw new IOException();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public interface TransactionalDataSource extends Transactional2<IOException, SQLException> {
        DataSource dataSource();

        @Override
        default Connection connection() throws SQLException {
            return dataSource().getConnection();
        }

        static TransactionalDataSource using(DataSource dataSource) {
            return () -> dataSource;
        }
    }

    public interface Transactional1<E1 extends Exception>
            extends Transactional<E1, VoidException, VoidException, VoidException, VoidException> {
        // Alias
    }

    public interface Transactional2<E1 extends Exception, E2 extends Exception>
            extends Transactional<E1, E2, VoidException, VoidException, VoidException> {
        // Alias
    }

    public interface Transactional3<E1 extends Exception, E2 extends Exception, E3 extends Exception>
            extends Transactional<E1, E2, E3, VoidException, VoidException> {
        // Alias
    }

    public interface Transactional4<E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception>
            extends Transactional<E1, E2, E3, E4, VoidException> {
        // Alias
    }

    public interface Transactional5<E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception>
            extends Transactional<E1, E2, E3, E4, E5> {
        // Alias
    }

    public interface Transactional<E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception>
            extends WithConnection {

        default void with(ThrowingConsumer<Connection, E1, E2, E3, E4, E5> logic) throws SQLException, E1, E2, E3, E4, E5 {
            try (Connection connection = connection()) {
                logic.accept(connection);
            }
        }
    }

    public interface WithConnection {
        Connection connection() throws SQLException;
    }

    public interface ThrowingConsumer1<T, E1 extends Exception> extends ThrowingConsumer<T, E1, VoidException, VoidException, VoidException, VoidException> {
        // Alias
    }

    public interface ThrowingConsumer2<T, E1 extends Exception, E2 extends Exception> extends ThrowingConsumer<T, E1, E2, VoidException, VoidException, VoidException> {
        // Alias
    }

    public interface ThrowingConsumer3<T, E1 extends Exception, E2 extends Exception, E3 extends Exception> extends ThrowingConsumer<T, E1, E2, E3, VoidException, VoidException> {
        // Alias
    }

    public interface ThrowingConsumer4<T, E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception> extends ThrowingConsumer<T, E1, E2, E3, E4, VoidException> {
        // Alias
    }

    public interface ThrowingConsumer5<T, E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception> extends ThrowingConsumer<T, E1, E2, E3, E4, E5> {
        // Alias
    }

    public interface ThrowingConsumer<T, E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception> {

        /**
         * Performs this operation on the given argument.
         *
         * @param input the input to consume
         * @throws E1 if unable to compute
         * @throws E2 if unable to compute
         * @throws E3 if unable to compute
         * @throws E4 if unable to compute
         * @throws E5 if unable to compute
         */
        void accept(T input) throws E1, E2, E3, E4, E5;
    }

    public enum Unit {
        INSTANCE
    }

}