package com.bluecatcode.common.base;

import com.google.common.base.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class NullablePredicate<T> implements Predicate<T> {
    @Override
    public boolean apply(@Nullable T input) {
        return input != null && applyNotNull(input);
    }

    public abstract boolean applyNotNull(@Nonnull T input);
}