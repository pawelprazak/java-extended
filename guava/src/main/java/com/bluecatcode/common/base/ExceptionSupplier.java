package com.bluecatcode.common.base;

import com.google.common.base.Supplier;

/**
 * Optional supplier for exceptions
 * <p/>
 * The supplier is designed to be used for more DLS usage of the following pattern:
 * {@code}
 * <pre>
 *  Optional<Something> optionalSomething = ...;
 *  if (!optional.isPresent()) {
 *      throw new SomeException();
 *  }
 *  Something something = optionalSomething.get();
 * </pre>
 * As shown here:
 * {@code}
 * <pre>
 *  Something something = optionalSomething.or(throwA(Something.class, new SomeException()));
 * </pre>
 *
 * @see com.google.common.base.Optional
 */
public class ExceptionSupplier<T, E extends RuntimeException> implements Supplier<T> {

    private final E exception;

    private ExceptionSupplier(E exception) {
        this.exception = exception;
    }

    /**
     * Factory method to initialise a variable or field
     *
     * @param exception exception to throw
     * @param <T>       Supplied object type
     * @param <E>       RuntimeException subtype
     * @throws E
     */
    public static <T, E extends RuntimeException> ExceptionSupplier<T, E> throwA(E exception) {
        return new ExceptionSupplier<T, E>(exception);
    }

    /**
     * Factory method for usage inside a method call
     *
     * @param class_    class used for type inference
     * @param exception exception to throw
     * @param <T>       Supplied object type
     * @param <E>       RuntimeException subtype
     * @throws E
     */
    public static <T, E extends RuntimeException> ExceptionSupplier<T, E> throwA(@SuppressWarnings("UnusedParameters") Class<T> class_, E exception) {
        return new ExceptionSupplier<T, E>(exception);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public T get() {
        throw exception;
    }
}