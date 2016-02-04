package com.bluecatcode.common.base;

import com.bluecatcode.core.exceptions.UncheckedException;
import com.google.common.base.Function;

public class Exceptions {

    private Exceptions() {
        throw new UnsupportedOperationException();
    }

    public static Function<Exception, RuntimeException> uncheckedException() {
        return e -> e instanceof RuntimeException ? (RuntimeException) e : new UncheckedException(e);
    }

}
