package com.bluecatcode.common.contract;

import com.bluecatcode.common.contract.errors.EnsureViolation;
import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;

import static com.bluecatcode.common.contract.Checks.EMPTY_ERROR_MESSAGE_ARGS;
import static com.bluecatcode.common.contract.Checks.check;
import static java.lang.String.format;

/**
 * Postconditions that supplier are supposed to ensure.
 * Violations are considered to be programming errors, on the suppliers part.
 */
@Beta
public class Postconditions {

    private Postconditions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be a programming error,
     * on the suppliers part.
     *
     * @param condition the condition to check
     * @throws EnsureViolation if the <tt>condition</tt> is <b>false</b>
     */
    public static void ensure(boolean condition) {
        if (!condition) {
            throw new EnsureViolation("Condition expected to be true");
        }
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be a programming error, on the suppliers part.
     *
     * @param condition the assertion condition
     * @param message   the fail message
     * @throws EnsureViolation if the <tt>condition</tt> is <b>false</b>
     */
    public static void ensure(boolean condition, String message) {
        if (!condition) {
            throw new EnsureViolation(message);
        }
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be a programming error, on the suppliers part.
     *
     * @param condition the assertion condition
     * @param message   the fail message template
     * @param args      the message template arguments
     * @throws EnsureViolation if the <tt>condition</tt> is <b>false</b>
     */
    public static void ensure(boolean condition, String message, Object... args) {
        if (!condition) {
            throw new EnsureViolation(format(message, args));
        }
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be programming errors, on the suppliers part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @param message   the fail message template
     * @param args      the message template arguments
     * @return the validated object
     * @throws EnsureViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T ensure(T reference, Predicate<T> predicate,
                               @Nullable String message,
                               @Nullable Object... args) {
        return check(reference, predicate, EnsureViolation.class, message, args);
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be programming errors, on the suppliers part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @param message   the fail message
     * @return the validated object
     * @throws EnsureViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T ensure(T reference, Predicate<T> predicate, @Nullable String message) {
        return ensure(reference, predicate, String.valueOf(message), EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Postcondition that supplier are supposed to ensure.
     * Violations are considered to be programming errors, on the suppliers part.
     *
     * @param <T>       type of object to check
     * @param reference the reference to check
     * @param predicate the predicate that the given reference must satisfy
     * @return the validated object
     * @throws EnsureViolation if the <tt>predicate</tt> is <b>false</b>
     */
    public static <T> T ensure(T reference, Predicate<T> predicate) {
        return ensure(reference, predicate, "Expected to fulfill the ensurement, got '%s'", reference);
    }
}