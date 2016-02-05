package com.bluecatcode.common.contract;

import com.bluecatcode.common.contract.errors.RequireViolation;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;

import static com.bluecatcode.common.contract.Checks.EMPTY_ERROR_MESSAGE_ARGS;
import static com.bluecatcode.common.contract.Checks.check;
import static java.lang.String.format;

/**
 * Preconditions that clients are required to fulfill.
 * Violations are considered to be programming errors, on the clients part.
 *
 * @see RequireViolation
 */
public class Preconditions {

    private Preconditions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param condition the condition to check
     */
    public static void require(boolean condition) {
        if (!condition) {
            throw new RequireViolation("Expected the condition to be true");
        }
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param condition the condition to check
     * @param message   the fail message template
     * @throws RequireViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new RequireViolation(message);
        }
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param condition the condition to check
     * @param message   the fail message template
     * @param args      the message template arguments
     * @throws RequireViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static void require(boolean condition, String message, Object... args) {
        if (!condition) {
            throw new RequireViolation(format(message, args));
        }
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @param message   the fail message template
     * @param args      the message template arguments
     * @return the validated object
     * @throws RequireViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T require(T reference, Predicate<T> predicate,
                                @Nullable String message,
                                @Nullable Object... args) {
        return check(reference, predicate, RequireViolation.class, message, args);
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @param message   the fail message template
     * @return the validated object
     * @throws RequireViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T require(T reference, Predicate<T> predicate, @Nullable String message) {
        return require(reference, predicate, String.valueOf(message), EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Precondition that clients are required to fulfill.
     * Violations are considered to be programming errors, on the clients part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @return the validated object
     * @throws RequireViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T require(T reference, Predicate<T> predicate) {
        return require(reference, predicate, "Expected to fulfill the requirement, got '%s'", reference);
    }
}
