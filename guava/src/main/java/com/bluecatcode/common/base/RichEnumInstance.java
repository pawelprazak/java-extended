package com.bluecatcode.common.base;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation for {@link com.bluecatcode.common.base.RichEnum}
 * @see com.bluecatcode.common.base.RichEnum
 * @see com.bluecatcode.common.base.RichEnumConstants
 * @param <T> the enum type
 */
public class RichEnumInstance<T extends Enum<T> & RichEnum<T>> {

    private final T theEnum;

    public RichEnumInstance(T theEnum) {
        checkArgument(theEnum != null, "Expected non-null theEnum");
        this.theEnum = theEnum;
    }

    public static <T extends Enum<T> & RichEnum<T>> RichEnumInstance<T> richEnum(T theEnum) {
        return new RichEnumInstance<>(theEnum);
    }

    public boolean nameEquals(@Nullable String that) {
        return theEnum.name().equals(that);
    }

    public boolean nameEqualsIgnoreCase(@Nullable String that) {
        return theEnum.name().equalsIgnoreCase(that);
    }

    public boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that) {
        String selfNameWithoutUnderscore = checkNotNull(theEnum.name()).replace("_", "");
        String thatNameWithoutUnderscore = that == null ? null : that.replace("_", "");
        return selfNameWithoutUnderscore.equalsIgnoreCase(thatNameWithoutUnderscore);
    }
}
