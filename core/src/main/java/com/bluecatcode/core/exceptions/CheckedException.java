package com.bluecatcode.core.exceptions;

/**
 * Base CheckedException exception with a clear name
 * <p>
 * Intended for use with custom exceptions for clear class hierarchy
 *
 * @see Exception
 * @since 1.0.4
 */
public class CheckedException extends Exception {

    static final long serialVersionUID = -0L;

    /**
     * @see Exception#Exception()
     */
    public CheckedException() {
        /* Empty */
    }

    /**
     * @see Exception#Exception(String)
     */
    public CheckedException(String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public CheckedException(Throwable cause) {
        super(cause);
    }

}
