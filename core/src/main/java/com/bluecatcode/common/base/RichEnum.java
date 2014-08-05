package com.bluecatcode.common.base;

import javax.annotation.Nullable;

public interface RichEnum<T> {

    boolean nameEquals(@Nullable String that);

    boolean nameEqualsIgnoreCase(@Nullable String that);

    boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that);
}

