package com.bluecatcode.common.base;

/**
 * Checks if the input object is an instance of any of provided class(es)
 */
public class IsInstancePredicate<T> extends NullablePredicate<T> {

    private final Class[] types;

    public IsInstancePredicate(Class[] types) {
        this.types = types;
    }

    @Override
    public boolean applyNotNull(T input) {
        for (Class type : types) {
            boolean isInstance = type.isInstance(input);
            if (isInstance) {
                return true;
            }
        }
        return false;
    }

}
