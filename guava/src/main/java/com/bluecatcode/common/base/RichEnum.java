package com.bluecatcode.common.base;

import javax.annotation.Nullable;

/**
 * An interface for corresponding {@link RichEnumInstance} and {@link RichEnumConstants}
 * @see RichEnumInstance
 * @see RichEnumConstants
 * @param <T> the enum type
 */
public interface RichEnum<T> {

    boolean nameEquals(@Nullable String that);

    boolean nameEqualsIgnoreCase(@Nullable String that);

    boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that);
}

