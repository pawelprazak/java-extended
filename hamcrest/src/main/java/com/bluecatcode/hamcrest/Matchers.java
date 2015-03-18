package com.bluecatcode.hamcrest;

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
     */
    public static Matcher<String> isNotEmptyOrNullString() {
        return not(isEmptyOrNullString());
    }

    /**
     * Equivalent of {@code allOf(containsString(...), ...)} matcher combination
     * @see org.hamcrest.Matchers#allOf(Iterable)
     * @see org.hamcrest.Matchers#containsString(String)
     */
    public static Matcher<String> containsStrings(String... strings) {
        List<Matcher<? super String>> matchers = new ArrayList<>();
        for (String string : strings) {
            matchers.add(containsString(string));
        }
        return org.hamcrest.core.AllOf.allOf(matchers);
    }

    /**
     * Equivalent of {@code allOf(pattern(...), ...)} matcher combination
     * @see org.hamcrest.Matchers#allOf(Iterable)
     * @see Matchers#pattern(String)
     */
    public static Matcher<String> containsPatterns(String... patterns) {
        List<Matcher<? super String>> matchers = new ArrayList<>();
        for (String pattern : patterns) {
            matchers.add(pattern(pattern));
        }
        return org.hamcrest.core.AllOf.allOf(matchers);
    }

    /**
     * Equivalent of {@code not(isIn(Collection<T>)} matcher combination
     * @see org.hamcrest.Matchers#isIn(java.util.Collection)
     */
    public static <T> Matcher<T> isNotIn(Collection<T> collection) {
        return not(isIn(collection));
    }

    /**
     * Equivalent of {@code not(isIn(Collection<T>)} matcher combination
     * @see org.hamcrest.Matchers#isIn(Object[])
     */
    public static <T> Matcher<T> isNotIn(T[] array) {
        return not(isIn(array));
    }

    /**
     * Equivalent of {@code allOf(hasItem(T), ...)} matcher combination
     */
    public static <T> Matcher<Iterable<T>> hasItems(Iterable<T> items) {
        List<Matcher<? super Iterable<T>>> all = new LinkedList<>();
        for (T element : items) {
            all.add(hasItem(element));
        }

        return allOf(all);
    }

    /**
     * @see PatternMatcher#pattern(java.util.regex.Pattern)
     */
    public static Matcher<? super CharSequence> pattern(Pattern pattern) {
        return PatternMatcher.pattern(pattern);
    }

    /**
     * @see PatternMatcher#pattern(String)
     */
    public static Matcher<? super CharSequence> pattern(String pattern) {
        return PatternMatcher.pattern(pattern);
    }

    /**
     * {@inheritDoc}
     */
    public static Matcher<Long> closeTo(Long operand, Long error) {
        return LongCloseTo.closeTo(operand, error);
    }

    public static <T extends Comparable<T>> Matcher<T> between(T min, T max) {
        return org.hamcrest.Matchers.allOf(greaterThan(min), lessThan(max));
    }

    public static Matcher<Map<?, ?>> isAnEmptyMap() {
        return org.hamcrest.Matchers.allOf(notNullValue(), not(hasEntry(notNullValue(), notNullValue())));
    }

    private Matchers() {
        throw new UnsupportedOperationException();
    }

}
