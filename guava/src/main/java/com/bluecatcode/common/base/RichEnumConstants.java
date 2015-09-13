package com.bluecatcode.common.base;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class RichEnumConstants<T extends Enum & RichEnum> {

    private final ImmutableList<T> values;

    public RichEnumConstants(Class<T> theClass) {
        T[] constants = checkNotNull(checkNotNull(theClass).getEnumConstants());
        this.values = ImmutableList.copyOf(constants);
    }

    public static <T extends Enum & RichEnum> RichEnumConstants<T> richConstants(Class<T> theClass) {
        return new RichEnumConstants<>(theClass);
    }

    public FluentIterable<T> fluent() {
        return FluentIterable.from(values);
    }

    public FluentIterable<String> names() {
        return fluent().transform(v -> checkNotNull(v).name());
    }

    public String asString() {
        return Joiner.on(",").join(values);
    }

    public boolean contains(@Nullable T that) {
        return values.contains(that);
    }

    public boolean contains(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEquals(that));
    }

    public boolean containsIgnoreCase(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEqualsIgnoreCase(that));
    }

    public boolean containsIgnoreCaseAndUnderscore(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEqualsIgnoreCaseAndUnderscore(that));
    }

    private T valueThat(Predicate<? super T> predicate, final String message) {
        return fluent()
                .firstMatch(predicate)
                .or(() -> {
                    throw new IllegalArgumentException(message);
                });
    }

    public T valueOf(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEquals(that),
                format("Expected one of (exact): %s, got %s", values, that)
        );
    }

    public T valueOfIgnoreCase(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEqualsIgnoreCase(that),
                format("Expected one of (ignoring case): %s, got %s", values, that)
        );
    }

    public T valueOfIgnoreCaseAndUnderscore(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEqualsIgnoreCaseAndUnderscore(that),
                format("Expected one of (ignoring case and underscores): %s, got %s", values, that)
        );
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("values", values)
                .toString();
    }
}
