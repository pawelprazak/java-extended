package com.bluecatcode.core.exceptions;

/**
 * Base UncheckedException exception with a clear name
 * <p>
 * Intended for use with custom exceptions for clear class hierarchy
 *
 * @see RuntimeException
 * @since 1.0.4
 */
public class UncheckedException extends RuntimeException {

    static final long serialVersionUID = -0L;

    /**
     * @see RuntimeException#RuntimeException()
     */
    public UncheckedException() {
        /* Empty */
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public UncheckedException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public UncheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    public UncheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
