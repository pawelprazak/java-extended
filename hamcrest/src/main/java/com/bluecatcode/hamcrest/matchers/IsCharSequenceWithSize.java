package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

public class IsCharSequenceWithSize<E extends CharSequence> extends TypeSafeMatcher<E> {

    private final Matcher<? super Integer> sizeMatcher;

    public IsCharSequenceWithSize(Matcher<? super Integer> sizeMatcher) {
        this.sizeMatcher = sizeMatcher;
    }

    @Override
    protected boolean matchesSafely(E item) {
        return sizeMatcher.matches(item.length());
    }

    @Override
    public void describeMismatchSafely(CharSequence item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(String.valueOf(item)).appendText("\"");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string with size ")
                .appendDescriptionOf(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link java.lang.CharSequence} that matches when the <code>length()</code> method returns
     * a value that satisfies the specified matcher.
     * <p/>
     * For example:
     * <pre>assertThat("foo", hasSize(equalTo(3)))</pre>
     *
     * @param sizeMatcher
     *     a matcher for the size of an examined {@link java.lang.CharSequence}
     */
    @Factory
    public static <E extends CharSequence> Matcher<E> hasSize(Matcher<? super Integer> sizeMatcher) {
        return new IsCharSequenceWithSize<E>(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link java.lang.CharSequence} that matches when the <code>length()</code> method returns
     * a value equal to the specified <code>size</code>.
     * <p/>
     * For example:
     * <pre>assertThat("foo", hasSize(3))</pre>
     *
     * @param size the expected size of an examined {@link java.lang.CharSequence}
     */
    @Factory
    public static <E extends CharSequence> Matcher<E> hasSize(int size) {
        Matcher<? super Integer> matcher = equalTo(size);
        return IsCharSequenceWithSize.<E>hasSize(matcher);
    }
}