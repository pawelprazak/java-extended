package com.bluecatcode.common.base;

import com.bluecatcode.common.contract.errors.ContractViolation;
import com.google.common.base.Function;
import com.google.common.base.Objects;

import javax.annotation.Nullable;

import static com.bluecatcode.common.contract.Preconditions.require;
import static java.lang.String.format;

/**
 * Implementation of an {@link Either} containing a left reference.
 */
final class Left<L, R> extends Either<L, R> {

    private static final long serialVersionUID = 0L;

    private final L left;

    Left(L left) {
        require(left != null, "Expected non-null left");
        this.left = left;
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
        throw new ContractViolation("Right value is absent");
    }

    @Override
    public Either<L, R> or(Either<? extends L, ? extends R> secondChoice) {
        require(secondChoice != null, "Expected non-null secondChoice");
        //noinspection unchecked
        return (Either<L, R>) secondChoice;
    }

    @Override
    public <E extends Exception> R orThrow(Function<L, E> leftFunction) throws E {
        require(leftFunction != null, "Expected non-null leftFunction");
        E exception = leftFunction.apply(left());
        require(exception != null);
        throw exception;
    }

    @Override
    public <V> V either(Function<L, V> leftFunction, Function<R, V> rightFunction) {
        require(leftFunction != null, "Expected non-null leftFunction");
        return leftFunction.apply(left());
    }

    @Override
    public <A, B> Either<A, B> transform(Function<L, A> leftFunction, Function<R, B> rightFunction) {
        require(leftFunction != null, "Expected non-null leftFunction");
        //noinspection ConstantConditions
        return leftOf(leftFunction.apply(left()));
    }

    @Override
    public Either<R, L> swap() {
        return rightOf(left());
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Left<?, ?> left1 = (Left<?, ?>) other;
        return Objects.equal(left, left1.left);
    }

    @Override
    public int hashCode() {
        return 0x43e36ed9 + left.hashCode();
    }

    @Override
    public String toString() {
        return format("Left.of(%s)", left);
    }
}
