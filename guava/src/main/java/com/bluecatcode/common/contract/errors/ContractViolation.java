package com.bluecatcode.common.contract.errors;

/**
 * Exception thrown to indicate that a contract violation have been detected.
 * Contract violations are considered to be programming errors, and hence
 * should not be caught and recovered from in normal application flow.
 */
public class ContractViolation extends Error {

    private static final long serialVersionUID = 0L;

    public ContractViolation() {
        /* Empty */
    }

    public ContractViolation(String message) {
        super(message);
    }

    public ContractViolation(String message, Throwable cause) {
        super(message, cause);
    }

    public ContractViolation(Throwable cause) {
        super(cause);
    }
}