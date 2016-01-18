package com.bluecatcode.common.base;

import javax.annotation.Nullable;

/**
 * Implementation of an {@link Either} containing a left reference.
 */
final class Left<L, R> extends Either<L, R> {

    private final L left;

    Left(L left) {
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
        throw new IllegalStateException("Right value is absent");
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
