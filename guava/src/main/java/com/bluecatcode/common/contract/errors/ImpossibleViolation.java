package com.bluecatcode.common.contract.errors;

/**
 * Exception being thrown to indicate that supposedly unreachable code
 * have been reached, and that this is considered a programming error.
 */
public class ImpossibleViolation extends ContractViolation {

    private static final long serialVersionUID = 0L;

    public ImpossibleViolation() {
        /* Empty */
    }

    public ImpossibleViolation(String message) {
        super(message);
    }

    public ImpossibleViolation(String message, Throwable cause) {
        super(message, cause);
    }

    public ImpossibleViolation(Throwable cause) {
        super(cause);
    }
}