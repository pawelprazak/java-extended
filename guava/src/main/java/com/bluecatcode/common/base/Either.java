package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a value of one of two possible types (a disjoint union). Instances of {@code Either} are either an instance of {@code Left} or {@code Right}.
 *
 * Either is an algebraic data type similar to the {@link com.google.common.base.Optional}.
 *
 * A common use of {@code Either} is as an alternative to {@code Optional} for dealing with possible missing values.
 * In this usage, {@code Absent} is replaced with a {@code Left} which can contain useful information.
 * {@code Right} takes the place of {@code Present}.
 * Convention dictates that {@code Left} is used for <b>failure</b> and {@code Right} is used for <b>success</b>.
 *
 * A non-null {@code Either<L,R>} reference can be used as an alternative to the classic error handling (exceptions).
 *
 * <pre>
 *  public static Either<Exception, Integer> divide(int x, int y) {
 *    try {
 *      return Either.valueOf(x / y);
 *    } catch (Exception e) {
 *      return Either.errorOf(e);
 *    }
 *  }
 * </pre>
 *
 * @param <L> the type of left instance that can be contained.
 * @param <R> the type of right instance that can be contained.
 * @since 1.0.5
 */
@Beta
@CheckReturnValue
public abstract class Either<L, R> implements Serializable {

    private static final long serialVersionUID = 0;

    Either() {
        /* Empty */
    }

    /**
     * Same as {@link #rightOf}, a convenience method for Value/Error use case.
     */
    public static <L, R> Either<L, R> valueOf(R reference) {
        return rightOf(reference);
    }

    /**
     * Same as {@link #leftOf}, a convenience method for Value/Error use case.
     */
    public static <L, R> Either<L, R> errorOf(L reference) {
        return leftOf(reference);
    }

    /**
     * Returns an {@code Either} instance containing the given non-null reference.
     *
     * @throws NullPointerException if {@code reference} is null
     */
    public static <L, R> Either<L, R> leftOf(L reference) {
        return new Left<>(checkNotNull(reference));
    }

    /**
     * Returns an {@code Either} instance containing the given non-null reference.
     *
     * @throws NullPointerException if {@code reference} is null
     */
    public static <L, R> Either<L, R> rightOf(R reference) {
        return new Right<>(checkNotNull(reference));
    }

    /**
     * Same as {@link #isRight()}, a convenience method for Value/Error use case.
     */
    public boolean isValue() {
        return isRight();
    }

    /**
     * Same as {@link #isLeft}, a convenience method for Value/Error use case.
     */
    public boolean isError() {
        return isLeft();
    }

    /**
     * @return true if this is a Left, false otherwise.
     */
    public abstract boolean isLeft();

    /**
     * @return true if this is a Right, false otherwise.
     */
    public abstract boolean isRight();

    /**
     * Same as {@link #right()}, a convenience method for Value/Error use case.
     */
    public R value() {
        return right();
    }

    /**
     * Same as {@link #left()}, a convenience method for Value/Error use case.
     */
    public L error() {
        return left();
    }

    /**
     * Returns the contained instance, which must be present.
     *
     * @throws IllegalStateException if the instance is absent ({@link #isLeft()} returns
     *     {@code false}); depending on this <i>specific</i> exception type (over the more general
     *     {@link RuntimeException}) is discouraged
     */
    public abstract L left();

    /**
     * Returns the contained instance, which must be present.
     *
     * @throws IllegalStateException if the instance is absent ({@link #isRight()} returns
     *     {@code false}); depending on this <i>specific</i> exception type (over the more general
     *     {@link RuntimeException}) is discouraged
     */
    public abstract R right();

    /**
     * Returns this {@code Either} if it has the right value present; {@code secondChoice} otherwise.
     */
    public abstract Either<L, R> or(Either<? extends L, ? extends R> secondChoice);

    /**
     * Returns the left instance if it is present; {@code throw leftFunction.apply(left())} otherwise.
     *
     * @throws NullPointerException if right value is absent and the function returns {@code null}
     */
    public abstract <E extends RuntimeException> R or(Function<L, E> leftFunction);

    /**
     * Applies {@code leftFunction} if this is a Left or {@code rightFunction if this is a Right.
     *
     * @return the result of the {@code function}.
     * @throws NullPointerException if the function returns {@code null}
     */
    public abstract <V> V either(Function<L, V> leftFunction, Function<R, V> rightFunction);

    /**
     * Applies {@code leftFunction} if this is a Left or {@code rightFunction if this is a Right.
     *
     * @return the result of the {@code function}.
     * @throws NullPointerException if the function returns {@code null}
     */
    public abstract <A, B> Either<A, B> transform(Function<L, A> leftFunction, Function<R, B> rightFunction);

    /**
     * If this is a Left, then return the left value in Right or vice versa.
     *
     * @return a new {@code Either<R,L>}
     */
    public abstract Either<R, L> swap();

    /**
     * Returns {@code true} if {@code object} is an {@code Either} instance, and either
     * the contained references are {@linkplain Object#equals equal} to each other.
     * Note that {@code Either} instances of differing parameterized types can
     * be equal.
     */
    @Override
    public abstract boolean equals(@Nullable Object object);

    /**
     * Returns a hash code for this instance.
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns a string representation for this instance.
     */
    @Override
    public abstract String toString();

}
