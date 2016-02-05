package com.bluecatcode.common.contract.errors;

/**
 * Exception thrown to indicate that a programming error have been detected,
 * on the clients part. A pre-condition does not hold.
 */
public class RequireViolation extends ContractViolation {

    private static final long serialVersionUID = 0L;

    public RequireViolation() {
        /* Empty */
    }

    public RequireViolation(String message) {
        super(message);
    }

    public RequireViolation(String message, Throwable cause) {
        super(message, cause);
    }

    public RequireViolation(Throwable cause) {
        super(cause);
    }
}