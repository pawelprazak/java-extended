package com.bluecatcode.common.exceptions;

public class VoidException extends RuntimeException {
    private VoidException() {
        throw new UnsupportedOperationException();
    }
}