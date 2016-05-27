package com.bluecatcode.common.exceptions

import com.bluecatcode.common.contract.errors.RequireViolation
import spock.lang.FailsWith
import spock.lang.Specification

class WrappedExceptionSpec extends Specification {

    @FailsWith(RequireViolation)
    def "should throw on null wrap argument"() {
        expect:
        WrappedException.wrap(null)
    }

    @FailsWith(RequireViolation)
    def "should throw on null unwrapAs argument"() {
        expect:
        WrappedException.wrap(new Exception()).unwrapAs(null)
    }

    def "should unwrap"() {
        given:
        def cause = new IllegalArgumentException("test");

        expect:
        def wrappedException = WrappedException.wrap(cause)
        wrappedException.unwrap() == cause
        wrappedException.unwrapAs(IllegalArgumentException) == cause
    }
}
