package com.bluecatcode.common.exceptions;

public class VoidException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    private VoidException() {
        throw new UnsupportedOperationException();
    }
}