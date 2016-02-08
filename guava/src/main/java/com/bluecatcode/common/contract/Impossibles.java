package com.bluecatcode.common.contract;

import com.bluecatcode.common.contract.errors.ImpossibleViolation;

import static java.lang.String.format;

/**
 * Impossibilities Unreachable code have been reached.
 * This is considered to be a programming error.
 */
public class Impossibles {

    private Impossibles() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unreachable code have been reached.
     * This is considered to be a programming error.
     *
     * @throws ImpossibleViolation unconditionally
     */
    public static void impossible() {
        throw new ImpossibleViolation("The impossible just happened");
    }

    /**
     * Unreachable code have been reached.
     * This is considered to be a programming error.
     *
     * @param cause the throwable cause
     * @throws ImpossibleViolation unconditionally
     */
    public static void impossible(Throwable cause) {
        throw new ImpossibleViolation("The impossible just happened", cause);
    }

    /**
     * Unreachable code have been reached.
     * This is considered to be a programming error.
     *
     * @param message the fail message template
     * @throws ImpossibleViolation unconditionally
     */
    public static void impossible(String message) {
        throw new ImpossibleViolation(message);
    }

    /**
     * Unreachable code have been reached.
     * This is considered to be a programming error.
     *
     * @param message the fail message template
     * @param args    the message template arguments
     * @throws ImpossibleViolation unconditionally
     */
    public static void impossible(String message, Object... args) {
        throw new ImpossibleViolation(format(message, args));
    }
}
