package com.bluecatcode.common.base;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;

import static com.bluecatcode.common.base.ExceptionSupplier.throwA;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * A companion class for {@link com.bluecatcode.common.base.RichEnum}
 * @see com.bluecatcode.common.base.RichEnum
 * @see com.bluecatcode.common.base.RichEnumInstance
 * @param <T>
 */
public class RichEnumConstants<T extends Enum & RichEnum> {

    private final ImmutableList<T> values;

    /**
     * @param theClass the enum class
     */
    public RichEnumConstants(Class<T> theClass) {
        T[] constants = checkNotNull(checkNotNull(theClass).getEnumConstants());
        this.values = ImmutableList.copyOf(constants);
    }

    /**
     * The factory method
     * @param theClass the enum class
     * @param <T> the enum type
     * @return the rich enum companion class instance
     */
    public static <T extends Enum & RichEnum> RichEnumConstants<T> richConstants(Class<T> theClass) {
        return new RichEnumConstants<>(theClass);
    }

    /**
     * @return a fluent iterable from the {@link java.lang.Class#getEnumConstants()}
     */
    public FluentIterable<T> fluent() {
        return FluentIterable.from(values);
    }

    /**
     * @return all names of the enums
     */
    public FluentIterable<String> names() {
        return fluent().transform(v -> checkNotNull(v).name());
    }

    /**
     * @return all values as comma separated list
     */
    public String asString() {
        return Joiner.on(",").join(values);
    }

    /**
     * Checks if a reference belongs to the enum
     * @param that reference to check
     * @return true if enum contains the reference
     */
    public boolean contains(@Nullable T that) {
        return values.contains(that);
    }

    /**
     * @param that the enum name
     * @return true if there is enum corresponding with the name
     */
    public boolean contains(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEquals(that));
    }

    /**
     * @param that the enum name
     * @return true if enum contains the name ignoring case
     */
    public boolean containsIgnoreCase(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEqualsIgnoreCase(that));
    }

    /**
     * @param that the enum name
     * @return true if enum contains the name ignoring case and underscores
     */
    public boolean containsIgnoreCaseAndUnderscore(@Nullable final String that) {
        return fluent().anyMatch(v -> checkNotNull(v).nameEqualsIgnoreCaseAndUnderscore(that));
    }

    /**
     * @param that the enum name
     * @return the enum instance corresponding with the name
     * @throws java.lang.IllegalArgumentException if the enum cannot be found
     */
    public T valueOf(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEquals(that),
                format("Expected one of (exact): %s, got %s", values, that)
        );
    }

    /**
     * @param that the enum name
     * @return the enum instance corresponding with the name ignoring case
     * @throws java.lang.IllegalArgumentException if the enum cannot be found
     */
    public T valueOfIgnoreCase(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEqualsIgnoreCase(that),
                format("Expected one of (ignoring case): %s, got %s", values, that)
        );
    }

    /**
     * @param that the enum name
     * @return the enum instance corresponding with the name ignoring case and underscores
     * @throws java.lang.IllegalArgumentException if the enum cannot be found
     */
    public T valueOfIgnoreCaseAndUnderscore(@Nullable final String that) {
        return valueThat(v -> checkNotNull(v).nameEqualsIgnoreCaseAndUnderscore(that),
                format("Expected one of (ignoring case and underscores): %s, got %s", values, that)
        );
    }

    private T valueThat(Predicate<? super T> predicate, final String message) {
        return fluent()
                .firstMatch(predicate)
                .or(throwA(new IllegalArgumentException(message)));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("values", values)
                .toString();
    }
}
