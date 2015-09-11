package com.bluecatcode.common.base;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public abstract class AbstractRichEnum<T extends Enum & RichEnum> {

    public static class Constants<T extends Enum & RichEnum> {

        private final ImmutableList<T> values;

        public Constants(Class<T> theClass) {
            T[] constants = checkNotNull(checkNotNull(theClass).getEnumConstants());
            this.values = ImmutableList.copyOf(constants);
        }

        public static <T extends Enum & RichEnum> Constants<T> enumConstants(Class<T> theClass) {
            return new Constants<>(theClass);
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

    public abstract T self();

    public boolean nameEquals(@Nullable String that) {
        return self().name().equals(that);
    }

    public boolean nameEqualsIgnoreCase(@Nullable String that) {
        return self().name().equalsIgnoreCase(that);
    }

    public boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that) {
        String selfNameWithoutUnderscore = checkNotNull(self().name()).replace("_", "");
        String thatNameWithoutUnderscore = that == null ? null : that.replace("_", "");
        return selfNameWithoutUnderscore.equalsIgnoreCase(thatNameWithoutUnderscore);
    }
}
