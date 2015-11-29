package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;

import static java.lang.String.format;

@Beta
public class Postconditions {

    /**
     * Fail with an AssertionError();
     *
     * @throws AssertionError every time
     */
    public static void fail() {
        throw new AssertionError();
    }

    /**
     * Fail with an AssertionError and a message.
     *
     * @param message the fail message
     * @throws AssertionError every time
     */
    public static void fail(String message) {
        throw new AssertionError(message);
    }

    /**
     * Fail with an AssertionError and a message which may use String.format if any
     * subsequent parameters are provided.
     *
     * @param message the fail message template
     * @param args the message template arguments
     * @throws AssertionError every time
     */
    public static void fail(String message, Object... args) {
        throw new AssertionError(format(message, args));
    }

    /**
     * Test the condition and throw an AssertionError if false with
     * a stock message.
     *
     * @param condition the assertion condition
     * @throws AssertionError if the <tt>condition</tt> is <b>false</b>
     */
    public static void assertTrue(boolean condition) {
        if (!condition) {
            fail("Condition expected to be true but was false.");
        }
    }

    /**
     * Test the condition and throw an AssertionError with a
     * provided message.
     *
     * @param condition the assertion condition
     * @param message the fail message
     * @throws AssertionError if the <tt>condition</tt> is <b>false</b>
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    /**
     * Test the condition and throw an AssertionError if false with
     * a stock message.
     *
     * @param condition the assertion condition
     * @param message the fail message template
     * @param args the message template arguments
     * @throws AssertionError if the <tt>condition</tt> is <b>false</b>
     */
    public static void assertTrue(boolean condition, String message, Object... args) {
        if (!condition) {
            fail(message, args);
        }
    }

    /**
     * Test the negation of the condition and throw an AssertionError with a
     * canned message.
     *
     * @param condition the assertion condition
     * @throws AssertionError if the <tt>condition</tt> is <b>true</b>
     */
    public static void assertFalse(boolean condition) {
        assertTrue(!condition, "Condition expected to be false but was true.");
    }

    /**
     * Test the negation of the condition and throw an AssertionError with a
     * provided message.
     *
     * @param condition the assertion condition
     * @param message the fail message
     * @throws AssertionError if the <tt>condition</tt> is <b>true</b>
     */
    public static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    /**
     * Test the negation of the condition and throw an AssertionError with a
     * canned message.
     *
     * @param condition the assertion condition
     * @param message the fail message template
     * @param args the message template arguments
     * @throws AssertionError if the <tt>condition</tt> is <b>true</b>
     */
    public static void assertFalse(boolean condition, String message, Object... args) {
        assertTrue(!condition, message, args);
    }

    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @param reference the reference
     * @return the passed reference
     * @throws IllegalStateException if the reference is not <tt>null</tt>
     */
    public static <T> T assertNull(@Nullable T reference) {
        return assertNull(reference, "Expected null but found '%s'.", reference);
    }


    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @param reference the reference
     * @param format the fail message template
     * @param args the message template arguments
     * @return the passed reference
     * @throws IllegalStateException if the reference is not <tt>null</tt>
     */
    public static <T> T assertNull(@Nullable T reference, String format, Object... args) {
        return assertNull(reference, format(format, args));
    }

    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @param reference the reference
     * @param message the fail message
     * @return the passed reference
     * @throws IllegalStateException if the reference is not <tt>null</tt>
     */
    public static <T> T assertNull(@Nullable T reference, String message) {
        if (reference != null) {
            fail(message);
        }
        throw new IllegalStateException("An AssertionError should have been thrown.");
    }

    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @throws IllegalStateException if the reference is <tt>null</tt>
     */
    public static <T> T assertNotNull(@Nullable T reference) {
        return assertNotNull(reference, "Null returned where expected non null result.");
    }

    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @param reference the reference
     * @param format the fail message template
     * @param args the message template arguments
     * @return the passed reference
     * @throws IllegalStateException if the reference is <tt>null</tt>
     */
    public static <T> T assertNotNull(@Nullable T reference, String format, Object... args) {
        return assertNotNull(reference, format(format, args));
    }

    /**
     * Assert that the provided reference is null else throw an AssertionError
     * with a canned error message.
     * @param reference the reference
     * @param message the fail message
     * @return the passed reference
     * @throws IllegalStateException if the reference is <tt>null</tt>
     */
    public static <T> T assertNotNull(@Nullable T reference, String message) {
        if (reference == null) {
            fail(message);
        }
        return reference;
    }

    private Postconditions() {
        throw new UnsupportedOperationException();
    }

}