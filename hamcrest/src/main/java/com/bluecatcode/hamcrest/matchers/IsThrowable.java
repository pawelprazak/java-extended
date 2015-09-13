package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class IsThrowable<T extends Throwable> extends CustomTypeSafeMatcher<T> {

    private final Matcher<T> matcher;

    public IsThrowable(Matcher<T> matcher) {
        super(CustomMatchers.fixedDescription(matcher));
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return matcher.matches(item);
    }

    public void describeMismatchSafely(T item, Description mismatchDescription) {
        matcher.describeMismatch(item, mismatchDescription);
    }

    /**
     * Matches if value is a Throwable of type <tt>type</tt>
     *
     * @param type throwable type
     */
    @Factory
    public static <T extends Throwable> Matcher<T> isThrowable(Class<?> type) {
        final Matcher<T> typeMatcher = instanceOf(type);
        return new IsThrowable<>(typeMatcher);
    }

    /**
     * Matches if value is a Throwable of type <tt>type</tt> that matches the <tt>matcher</tt>
     *
     * @param type    throwable type
     * @param matcher throwable matcher
     */
    @Factory
    public static <T extends Throwable> Matcher<T> isThrowable(Class<?> type, Matcher<? super T> matcher) {
        final Matcher<T> typeMatcher = instanceOf(type);
        return new IsThrowable<>(allOf(typeMatcher, matcher));
    }

    /**
     * Matches if value is a throwable with the <tt>message</tt>
     *
     * @param message message to match
     */
    @Factory
    public static <T extends Throwable> Matcher<T> withMessage(String message) {
        return withMessage(is(message));
    }

    /**
     * Matches if value is a throwable with a message that matches the <tt>matcher</tt>
     *
     * @param matcher message matcher
     */
    @Factory
    public static <T extends Throwable> Matcher<T> withMessage(final Matcher<String> matcher) {
        return new CustomTypeSafeMatcher<T>(CustomMatchers.fixedDescription("message ", matcher)) {
            @Override
            protected boolean matchesSafely(T item) {
                return matcher.matches(item.getMessage());
            }

            public void describeMismatchSafely(T item, Description mismatchDescription) {
                matcher.describeMismatch(item.getMessage(), mismatchDescription);
            }
        };
    }

    /**
     * Matches if value is a throwable with a cause that matches the <tt>matcher</tt>
     *
     * @param matcher cause matcher
     */
    @Factory
    public static <T extends Throwable, C extends Throwable> Matcher<T> withCause(final Matcher<C> matcher) {
        return new CustomTypeSafeMatcher<T>(CustomMatchers.fixedDescription("cause ", matcher)) {
            @Override
            protected boolean matchesSafely(T item) {
                return matcher.matches(item.getCause());
            }

            public void describeMismatchSafely(T item, Description mismatchDescription) {
                matcher.describeMismatch(item.getCause(), mismatchDescription);
            }
        };
    }
}