package com.bluecatcode.common.base.predicates;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Checks if the input object is an instance of any of provided class(es)
 */
@Beta
public class IsInstancePredicate<T> implements Predicate<T> {

    private final Class[] types;

    public IsInstancePredicate(Class[] types) {
        this.types = Arrays.copyOf(types, types.length);
    }

    @Override
    public boolean apply(@Nullable T input) {
        if (input == null) {
            return false;
        }
        for (Class type : types) {
            boolean isInstance = type.isInstance(input);
            if (isInstance) {
                return true;
            }
        }
        return false;
    }

}
