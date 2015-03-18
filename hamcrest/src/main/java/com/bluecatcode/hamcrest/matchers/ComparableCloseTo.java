package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class ComparableCloseTo<T extends Comparable<T>> extends TypeSafeMatcher<T> {

    protected final Long value;
    protected final Long delta;

    public ComparableCloseTo(Long error, Long value) {
        this.delta = error;
        this.value = value;
    }

    /**
     * @return the object to be compared to, usually a 'zero'
     * @see #actualDelta(Comparable)
     */
    protected abstract T zero();

    /**
     * @return the actual difference to a 'zero'
     * @see #zero()
     */
    protected abstract T actualDelta(T item);

    @Override
    public boolean matchesSafely(T item) {
        return actualDelta(item).compareTo(zero()) <= 0;
    }

    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendValue(item)
                .appendText(" differed by ")
                .appendValue(actualDelta(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a numeric value within ")
                .appendValue(delta)
                .appendText(" of ")
                .appendValue(value);
    }

}
