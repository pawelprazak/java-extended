package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nullable;

public class MatcherBuilder<T> extends TypeSafeMatcher<T> {
    protected final T item;
    private final ItemMatcher<T> itemMatcher;
    private final ObjectDescriber<T> objectDescriber;
    private final MismatchDescriber<T> mismatchDescriber;

    public MatcherBuilder(T item,
                          ItemMatcher<T> itemMatcher,
                          ObjectDescriber<T> objectDescriber,
                          MismatchDescriber<T> mismatchDescriber) {
        if (itemMatcher == null) {
            throw new IllegalArgumentException("Expected an item matcher");
        }
        if (objectDescriber == null) {
            throw new IllegalArgumentException("Expected an object description");
        }
        if (mismatchDescriber == null) {
            throw new IllegalArgumentException("Expected an mismatch describer");
        }
        this.item = item;
        this.itemMatcher = itemMatcher;
        this.objectDescriber = objectDescriber;
        this.mismatchDescriber = mismatchDescriber;
    }

    @Override
    protected boolean matchesSafely(@Nullable T item) {
        return itemMatcher.match(item);
    }

    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescriber.describe(item, mismatchDescription);
    }

    @Override
    public void describeTo(Description description) {
        objectDescriber.describe(item, description);
    }

    interface ItemMatcher<T> {
        boolean match(@Nullable T item);
    }

    interface MismatchDescriber<T> {
        Description describe(@Nullable T item, Description mismatchDescription);
    }

    interface ObjectDescriber<T> {
        Description describe(@Nullable T item, Description description);
    }

    @Factory
    public static <T> Matcher<T> newMatcher(T item,
                                            ItemMatcher<T> itemMatcher,
                                            ObjectDescriber<T> objectDescriber,
                                            MismatchDescriber<T> mismatchDescriber) {
        return new MatcherBuilder<>(item, itemMatcher, objectDescriber, mismatchDescriber);
    }

    @Factory
    public static <T> Matcher<T> newMatcher(T item,
                                            ItemMatcher<T> itemMatcher) {
        return new MatcherBuilder<>(item, itemMatcher,
                (o, description) -> description.appendText(o == null ? "null" : o.getClass().getSimpleName()),
                (i, mismatchDescription) -> mismatchDescription.appendText("was ").appendValue(i)
        );
    }
}
