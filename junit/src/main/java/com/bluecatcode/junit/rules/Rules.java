package com.bluecatcode.junit.rules;

import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import static com.bluecatcode.junit.rules.AssumeMore.assumeIsUnix;

public class Rules {

    public static RepeatRule repeatRule() {
        return new RepeatRule();
    }

    public static RepeatUntilExpectedExceptionRule repeatUntilExpectedExceptionRule() {
        return new RepeatUntilExpectedExceptionRule();
    }

    /**
     * Require Unix like operating system.
     */
    public static TestRule ignoreIfNotUnix() {
        return (base, description) -> new Statement() {
            @Override
            public void evaluate() throws Throwable {
                assumeIsUnix();
                base.evaluate();
            }
        };
    }
}
