package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * Matches a matcher against a CharSequence slice.
 */
public class CharSequenceSliceMatcher extends TypeSafeMatcher<CharSequence> {
    private int start;
    private int end;
    private final Matcher<CharSequence> matcher;

    /**
     * A slice matcher in range <tt>start</tt>, <tt>end</tt> inclusive.
     * Slice range can be from <tt>0</tt> to <tt>length</tt>, or from <tt>0</tt> to <tt>-1</tt>.
     * Negative index value denotes the element number counting from the last element,
     * starting with -1 as the last element, e.g.:
     * <pre>
     * start | end | sequence | result
     *     0 |   0 | "aaa"    | "a"
     *     0 |   1 | "aaa"    | "aa"
     *     0 |   2 | "aaa"    | "aaa"
     *     0 |  -1 | "aaa"    | "aaa"
     *     0 |  -2 | "abc"    | "ab"
     *     0 |  -3 | "abc"    | "a"
     *     1 |  -2 | "abc"    | "b"
     *     2 |  -1 | "abc"    | "c"
     *     2 |  -2 | "abc"    | ""
     *     1 |  -3 | "abc"    | ""
     *    -3 |  -3 | "abc"    | "a"
     *    -2 |  -2 | "abc"    | "b"
     *    -1 |  -1 | "abc"    | "c"
     *    -3 |  -2 | "abc"    | "ab"
     *    -2 |  -1 | "abc"    | "bc"
     *    -3 |  -1 | "abc"    | "abc"
     * </pre>
     * @param start the slice start, inclusive, can be negative
     * @param end the slice end, inclusive, can be negative
     * @param matcher the nested matcher to use with the slice
     */
    public CharSequenceSliceMatcher(int start, int end, Matcher<CharSequence> matcher) {
        if (matcher == null) {
            throw new IllegalArgumentException("Expected a matcher");
        }
        this.start = start;
        this.end = end;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(CharSequence item) {
        return matcher.matches(sliceCharSequence(start, end, item));
    }

    @Nullable
    static CharSequence sliceCharSequence(int start, int end, @Nullable CharSequence item) {
        if (item == null) {
            return null;
        }
        if (end > item.length()) {
            throw new IllegalArgumentException(format("Expected end to be less than length (%s)", item.length()));
        }
        if (start < 0) {
            start = item.length() + start;
        }
        if (end < 0) {
            end = item.length() + end;
        }
        return item.subSequence(start, end + 1 /* make it inclusive */ );
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText(format("a slice [%s:%s] the same as value", start, end))
                .appendText(" that ")
                .appendDescriptionOf(matcher);
    }

    @Override
    protected void describeMismatchSafely(CharSequence item, Description mismatchDescription) {
        super.describeMismatchSafely(item, mismatchDescription);
        CharSequence slice = sliceCharSequence(start, end, item);
        mismatchDescription.appendText(format(", slice: \"%s\"", slice));
    }

    /**
     * Match in range <tt>start</tt>, <tt>end</tt> inclusive
     * @see CharSequenceSliceMatcher#CharSequenceSliceMatcher(int, int, Matcher)
     * @param start the slice start, inclusive, can be negative
     * @param end the slice end, inclusive, can be negative
     * @param matcher the nested matcher to use with the slice
     * @return the matcher
     */
    @Factory
    public static Matcher<CharSequence> sliceThat(int start, int end, Matcher<CharSequence> matcher) {
        return new CharSequenceSliceMatcher(start, end, matcher);
    }

    /**
     * First element matcher
     * @param matcher the first element matcher
     * @return the matcher
     */
    @Factory
    public static Matcher<CharSequence> firstThat(Matcher<CharSequence> matcher) {
        return new CharSequenceSliceMatcher(0, 0, matcher);
    }

    /**
     * Last element matcher
     * @param matcher the last element matcher
     * @return the matcher
     */
    @Factory
    public static Matcher<CharSequence> lastThat(Matcher<CharSequence> matcher) {
        return new CharSequenceSliceMatcher(-1, -1, matcher);
    }

    /**
     * First element matcher
     * @param matcher the first element matcher
     * @return the matcher
     */
    @Factory
    public static Matcher<CharSequence> headThat(Matcher<CharSequence> matcher) {
        return new CharSequenceSliceMatcher(0, 0, matcher);
    }

    /**
     * Tail elements matcher (all except the first element)
     * @param matcher the tail elements matcher
     * @return the matcher
     */
    @Factory
    public static Matcher<CharSequence> tailThat(Matcher<CharSequence> matcher) {
        return new CharSequenceSliceMatcher(1, -1, matcher);
    }
}