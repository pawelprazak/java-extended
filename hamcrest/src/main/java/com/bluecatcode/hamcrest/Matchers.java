package com.bluecatcode.hamcrest;

import com.bluecatcode.hamcrest.matchers.IsCharSequenceWithSize;
import com.bluecatcode.hamcrest.matchers.IsThrowable;
import com.bluecatcode.hamcrest.matchers.LongCloseTo;
import com.bluecatcode.hamcrest.matchers.PatternMatcher;
import org.hamcrest.Matcher;

import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Additional Hamcrest matchers
 * @see org.hamcrest.Matchers
 */
public class Matchers {

    /**
     * Equivalent of {@code not(isEmptyOrNullString())} matcher combination
     * @see org.hamcrest.Matchers#isEmptyOrNullString()
     * @return the matcher
     */
    public static Matcher<String> isNotEmptyOrNullString() {
        return not(isEmptyOrNullString());
    }

    /**
     * Equivalent of {@code allOf(containsString(...), ...)} matcher combination
     * @see org.hamcrest.Matchers#allOf(Iterable)
     * @see org.hamcrest.Matchers#containsString(String)
     * @param strings all the strings to match against
     * @return the matcher
     */
    public static Matcher<String> containsStrings(String... strings) {
        List<Matcher<? super String>> matchers = new ArrayList<Matcher<? super String>>();
        for (String string : strings) {
            matchers.add(containsString(string));
        }
        return allOf(matchers);
    }

    /**
     * Equivalent of {@code allOf(pattern(...), ...)} matcher combination
     * @see org.hamcrest.Matchers#allOf(Iterable)
     * @see Matchers#pattern(String)
     * @param patterns all the patterns to match against
     * @return the matcher
     */
    public static Matcher<String> containsPatterns(String... patterns) {
        List<Matcher<? super String>> matchers = new ArrayList<Matcher<? super String>>();
        for (String pattern : patterns) {
            matchers.add(pattern(pattern));
        }
        return allOf(matchers);
    }

    /**
     * Equivalent of {@code not(isIn(Collection<T>)} matcher combination
     * @see org.hamcrest.Matchers#isIn(java.util.Collection)
     * @param collection the collection to match against
     * @param <T> the matcher type
     * @return the matcher
     */
    public static <T> Matcher<T> isNotIn(Collection<T> collection) {
        return not(isIn(collection));
    }

    /**
     * Equivalent of {@code not(isIn(Collection<T>)} matcher combination
     * @see org.hamcrest.Matchers#isIn(Object[])
     * @param array the array to match against
     * @param <T> the matcher type
     * @return the matcher
     */
    public static <T> Matcher<T> isNotIn(T[] array) {
        return not(isIn(array));
    }

    /**
     * Equivalent of {@code allOf(hasItem(T), ...)} matcher combination
     * @param items the items to match against
     * @param <T> the matcher type
     * @return the matcher
     */
    public static <T> Matcher<Iterable<T>> hasItems(Iterable<T> items) {
        List<Matcher<? super Iterable<T>>> all = new LinkedList<Matcher<? super Iterable<T>>>();
        for (T element : items) {
            all.add(hasItem(element));
        }

        return allOf(all);
    }

    /**
     * @see PatternMatcher#pattern(java.util.regex.Pattern)
     * @param pattern the pattern to match against
     * @return the matcher
     */
    public static Matcher<? super CharSequence> pattern(Pattern pattern) {
        return PatternMatcher.pattern(pattern);
    }

    /**
     * @see PatternMatcher#pattern(String)
     * @param pattern the pattern to match against
     * @return the matcher
     */
    public static Matcher<? super CharSequence> pattern(String pattern) {
        return PatternMatcher.pattern(pattern);
    }

    /**
     * @see LongCloseTo#closeTo(Long, Long)
     * @param operand the operand to match against
     * @param error the error margin
     * @return the matcher
     */
    public static Matcher<Long> closeTo(Long operand, Long error) {
        return LongCloseTo.closeTo(operand, error);
    }

    /**
     * Equivalent of {@code allOf(greaterThan(min), lessThan(max))} matcher combination
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @param <T> the matcher type
     * @return the matcher
     */
    public static <T extends Comparable<T>> Matcher<T> between(T min, T max) {
        return allOf(greaterThan(min), lessThan(max));
    }

    /**
     * Equivalent of {@code allOf(notNullValue(), not(hasEntry(notNullValue(), notNullValue())))} matcher combination
     * @return the matcher
     */
    public static Matcher<Map<?, ?>> isAnEmptyMap() {
        return allOf(notNullValue(), not(hasEntry(notNullValue(), notNullValue())));
    }

    /**
     * Throwable matcher with only a type argument
     * @param type the throwable type to match
     * @param <T> type of the matched throwable
     * @return the matcher
     */
    public static <T extends Throwable> Matcher<T> isThrowable(Class<?> type) {
        return IsThrowable.isThrowable(type);
    }

    /**
     * Throwable matcher with a type argument and additional matcher, e.g. the cause
     * @see #withCause(Matcher)
     * @see #withMessage(Matcher)
     * @param type the throwable type to match
     * @param matcher the additional matcher
     * @param <T> type of the matched throwable
     * @return the matcher
     */
    public static <T extends Throwable> Matcher<T> isThrowable(Class<?> type, Matcher<? super T> matcher) {
        return IsThrowable.isThrowable(type, matcher);
    }

    /**
     * Throwable matcher to be used with {@link #isThrowable(Class, Matcher)}
     * @see #isThrowable(Class, Matcher)}
     * @param matcher the cause matcher
     * @param <T> the throwable type
     * @param <C> the cause type
     * @return the matcher
     */
    public static <T extends Throwable, C extends Throwable> Matcher<T> withCause(Matcher<C> matcher) {
        return IsThrowable.withCause(matcher);
    }

    /**
     * Throwable matcher to be used with {@link #isThrowable(Class, Matcher)}
     * @see #isThrowable(Class, Matcher)}
     * @param matcher the message matcher
     * @param <T> the throwable type
     * @return the matcher
     */
    public static <T extends Throwable> Matcher<T> withMessage(Matcher<String> matcher) {
        return IsThrowable.withMessage(matcher);
    }

    /**
     * Throwable matcher to be used with {@link #isThrowable(Class, Matcher)}
     * @see #isThrowable(Class, Matcher)}
     * @param message the message
     * @param <T> the throwable type
     * @return the matcher
     */
    public static <T extends Throwable> Matcher<T> withMessage(String message) {
        return IsThrowable.withMessage(message);
    }

    /**
     * @see IsCharSequenceWithSize#hasSize(Matcher)
     * @param matcher the size matcher
     * @param <E> the char sequence type
     * @return the matcher
     */
    public static <E extends CharSequence> Matcher<E> hasSize(Matcher<? super Integer> matcher) {
        return IsCharSequenceWithSize.hasSize(matcher);
    }

    /**
     * @see IsCharSequenceWithSize#hasSize(int)
     * @param size the size to match against
     * @param <E> the char sequence type
     * @return the matcher
     */
    public static <E extends CharSequence> Matcher<E> hasSize(int size) {
        return IsCharSequenceWithSize.hasSize(size);
    }

    private Matchers() {
        throw new UnsupportedOperationException();
    }

}
