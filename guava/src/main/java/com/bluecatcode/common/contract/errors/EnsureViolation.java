package com.bluecatcode.common.contract.errors;

/**
 * Exception thrown to indicate that a programming error have been detected,
 * on the suppliers part. A post-condition does not hold.
 */
public class EnsureViolation extends ContractViolation {

    private static final long serialVersionUID = 0L;

    public EnsureViolation() {
        /* Empty */
    }

    public EnsureViolation(String message) {
        super(message);
    }

    public EnsureViolation(String message, Throwable cause) {
        super(message, cause);
    }

    public EnsureViolation(Throwable cause) {
        super(cause);
    }
}