package com.bluecatcode.common.base;

import javax.annotation.Nullable;

/**
 * An interface for corresponding {@link com.bluecatcode.common.base.RichEnumInstance}
 * and {@link com.bluecatcode.common.base.RichEnumConstants}
 * @see com.bluecatcode.common.base.RichEnumInstance
 * @see com.bluecatcode.common.base.RichEnumConstants
 * @param <T> the enum type
 */
public interface RichEnum<T> {

    boolean nameEquals(@Nullable String that);

    boolean nameEqualsIgnoreCase(@Nullable String that);

    boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that);
}

