package com.bluecatcode.common.base;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public interface RichEnum<T extends Enum & RichEnum> {

    static class Constants<T extends Enum & RichEnum> {

        private final ImmutableList<T> values;

        public Constants(Class<T> theClass) {
            T[] constants = checkNotNull(checkNotNull(theClass).getEnumConstants());
            this.values = ImmutableList.copyOf(constants);
        }

        public static <T extends Enum & RichEnum> Constants<T> enumConstants(Class<T> theClass) {
            return new Constants<T>(theClass);
        }

        public Stream<T> stream() {
            return values.stream();
        }

        public Stream<String> names() {
            return values.stream().map(T::name);
        }

        public String asString() {
            return Joiner.on(",").join(values);
        }

        public boolean contains(@Nullable T that) {
            return values.contains(that);
        }

        public boolean contains(@Nullable String that) {
            return stream().anyMatch(v -> v.nameEquals(that));
        }

        public boolean containsIgnoreCase(@Nullable String that) {
            return stream().anyMatch(v -> v.nameEqualsIgnoreCase(that));
        }

        public boolean containsIgnoreCaseAndUnderscore(@Nullable String that) {
            return stream().anyMatch(v -> v.nameEqualsIgnoreCaseAndUnderscore(that));
        }

        private T valueThat(Predicate<? super T> predicate, String message) {
            return stream()
                    .filter(predicate)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(message));
        }

        public T valueOf(@Nullable String that) {
            return valueThat(v -> v.nameEquals(that),
                    format("Expected one of (exact): %s, got %s", values, that));
        }

        public T valueOfIgnoreCase(@Nullable String that) {
            return valueThat(v -> v.nameEqualsIgnoreCase(that),
                    format("Expected one of (ignoring case): %s, got %s", values, that));
        }

        public T valueOfIgnoreCaseAndUnderscore(@Nullable String that) {
            return valueThat(v -> v.nameEqualsIgnoreCaseAndUnderscore(that),
                    format("Expected one of (ignoring case and underscores): %s, got %s", values, that));
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("values", values)
                    .toString();
        }
    }

    T self();

    default boolean nameEquals(@Nullable String that) {
        return self().name().equals(that);
    }

    default boolean nameEqualsIgnoreCase(@Nullable String that) {
        return self().name().equalsIgnoreCase(that);
    }

    default boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that) {
        String selfNameWithoutUnderscore = checkNotNull(self().name()).replace("_", "");
        String thatNameWithoutUnderscore = that == null ? null : that.replace("_", "");
        return selfNameWithoutUnderscore.equalsIgnoreCase(thatNameWithoutUnderscore);
    }
}