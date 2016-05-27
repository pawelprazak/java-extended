package com.bluecatcode.common.exceptions;

import com.bluecatcode.core.exceptions.UncheckedException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static com.bluecatcode.common.contract.Postconditions.ensure;
import static com.bluecatcode.common.contract.Preconditions.require;
import static com.bluecatcode.common.exceptions.Exceptions.*;

public final class WrappedException extends UncheckedException {

    private static final long serialVersionUID = 0L;

    public WrappedException(@Nonnull Exception cause) {
        super(cause);
        require(cause != null);
    }

    @CheckReturnValue
    public static WrappedException wrap(@Nonnull Exception cause) {
        return new WrappedException(cause);
    }

    @CheckReturnValue
    public static <E extends Exception> E wrap(@Nonnull Exception cause, Class<E> type) {
        return exception(type, parameters(Throwable.class), arguments(cause));
    }

    @CheckReturnValue
    public Exception unwrap() {
        ensure(this.getCause() != null);
        ensure(this.getCause() instanceof Exception);
        return (Exception) this.getCause();
    }

    @CheckReturnValue
    public <E extends Exception> E unwrap(@Nonnull Class<E> exceptionType) {
        ensure(this.getCause() != null);
        ensure(exceptionType.isAssignableFrom(this.getCause().getClass()));
        return exceptionType.cast(this.getCause());
    }
}