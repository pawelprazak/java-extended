package com.bluecatcode.common.base.predicates;

import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Checks if the input object is an instance of any of provided class(es)
 */
public class IsInstancePredicate implements Predicate<Object> {

    private final Class<?>[] types;

    public IsInstancePredicate(Class<?>[] types) {
        checkArgument(types != null, "Expected non-null types");
        this.types = Arrays.copyOf(types, types.length);
    }

    public static IsInstancePredicate isInstancePredicate(Class<?>[] types) {
        return new IsInstancePredicate(types);
    }

    @Override
    public boolean apply(@Nullable Object input) {
        if (input == null) {
            return false;
        }
        for (Class<?> type : types) {
            boolean isInstance = type.isInstance(input);
            if (isInstance) {
                return true;
            }
        }
        return false;
    }

}
