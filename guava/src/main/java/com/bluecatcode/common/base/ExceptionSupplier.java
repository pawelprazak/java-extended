package com.bluecatcode.common.base;

import com.google.common.base.Supplier;

/**
 * Optional supplier for exceptions
 * <p>
 * The supplier is designed to be used for more DLS usage of the following pattern:
 * </p>
 * {@code}
 * <pre>
 *  Optional&lt;Something&gt; optionalSomething = ...;
 *  if (!optional.isPresent()) {
 *      throw new SomeException();
 *  }
 *  Something something = optionalSomething.get();
 * </pre>
 * {@code}
 * As shown here:
 * <pre>
 *  Something something = optionalSomething.or(throwA(Something.class, new SomeException()));
 * </pre>
 *
 * @see com.google.common.base.Optional#or(Supplier)
 */
public class ExceptionSupplier<T, E extends RuntimeException> implements Supplier<T> {

    private final E exception;

    private ExceptionSupplier(E exception) {
        this.exception = exception;
    }

    /**
     * Factory method for usage during variable or field initialisation.
     *
     * @param exception exception to throw
     * @param <T>       Supplied object type
     * @param <E>       RuntimeException subtype
     * @return the exception supplier
     * @throws E the provided exception
     */
    public static <T, E extends RuntimeException> ExceptionSupplier<T, E> throwA(E exception) {
        return new ExceptionSupplier<>(exception);
    }

    /**
     * Factory method for usage inside a method call
     *
     * @param type      class type used only for type inference
     * @param exception exception to throw
     * @param <T>       Supplied object type
     * @param <E>       RuntimeException subtype
     * @return the exception supplier
     * @throws E the provided exception
     */
    public static <T, E extends RuntimeException> ExceptionSupplier<T, E> throwA(
            @SuppressWarnings("UnusedParameters") Class<T> type, E exception) {
        return new ExceptionSupplier<>(exception);
    }

    /**
     * @see #throwA(RuntimeException)
     * @param message the exception message
     * @return the exception supplier
     */
    public static ExceptionSupplier<String, IllegalArgumentException> throwIllegalArgumentException(String message) {
        return throwA(new IllegalArgumentException(message));
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public T get() {
        throw exception;
    }
}