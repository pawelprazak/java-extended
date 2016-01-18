package com.bluecatcode.common.base;

import javax.annotation.Nullable;

/**
 * Implementation of an {@link Either} containing a right reference.
 */
final class Right<L, R> extends Either<L, R> {

    private final R right;

    Right(R right) {
        this.right = right;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public L left() {
        throw new IllegalStateException("Left value is absent");
    }

    @Override
    public R right() {
        return this.right;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Right) {
            Right<?, ?> other = (Right<?, ?>) object;
            return right.equals(other.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0xc4dcd0b4 + right.hashCode();
    }

    @Override
    public String toString() {
        return "Either.of(" + right + ")";
    }
}
