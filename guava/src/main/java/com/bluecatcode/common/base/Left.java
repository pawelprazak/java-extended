package com.bluecatcode.common.base;

import com.google.common.base.Function;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of an {@link Either} containing a left reference.
 */
final class Left<L, R> extends Either<L, R> {

    private static final long serialVersionUID = 0L;

    private final L left;

    Left(L left) {
        this.left = checkNotNull(left, "Expected non-null left");
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public L left() {
        return this.left;
    }

    @Override
    public R right() {
        throw new IllegalStateException("Right value is absent");
    }

    @Override
    public Either<L, R> or(Either<? extends L, ? extends R> secondChoice) {
        checkArgument(secondChoice != null, "Expected non-null secondChoice");
        //noinspection unchecked
        return (Either<L, R>) secondChoice;
    }

    @Override
    public <E extends RuntimeException> R orThrow(Function<L, E> leftFunction) {
        checkArgument(leftFunction != null, "Expected non-null leftFunction");
        throw leftFunction.apply(left());
    }

    @Override
    public <V> V either(Function<L, V> leftFunction, Function<R, V> rightFunction) {
        checkArgument(leftFunction != null, "Expected non-null leftFunction");
        return leftFunction.apply(left());
    }

    @Override
    public <A, B> Either<A, B> transform(Function<L, A> leftFunction, Function<R, B> rightFunction) {
        checkArgument(leftFunction != null, "Expected non-null leftFunction");
        //noinspection ConstantConditions
        return leftOf(leftFunction.apply(left()));
    }

    @Override
    public Either<R, L> swap() {
        return rightOf(left());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Left) {
            Left<?, ?> other = (Left<?, ?>) object;
            return left.equals(other.left);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0x43e36ed9 + left.hashCode();
    }

    @Override
    public String toString() {
        return "Either.of(" + left + ")";
    }
}
