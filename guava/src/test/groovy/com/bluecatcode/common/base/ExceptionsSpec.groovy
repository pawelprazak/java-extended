package com.bluecatcode.common.base

import com.bluecatcode.common.functions.CheckedFunction
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.exceptions.Exceptions.*

class ExceptionsSpec extends Specification {

    @Unroll("#type.simpleName '#message' '#cause'")
    def "should create throwable"() {
        given:
        def exception0 = exception(type)
        def exception1 = exception(type, parameters(String.class), arguments(message))
        def exception2 = exception(type, parameters(String.class, Throwable.class), arguments(message, cause))

        expect:
        exception0 != null
        exception1 != null
        exception2 != null
        exception0.class == type
        exception1.class == type
        exception2.class == type
        exception1.message == message
        exception2.class == type
        exception2.message == message

        where:
        type                     | message        | cause
        IllegalArgumentException | null           | null
        IllegalArgumentException | "test message" | null
        IllegalArgumentException | "test message" | new IllegalArgumentException()
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("#type.simpleName '#parameters' '#args' -> throws IAE")
    def "should throw on illegal arguments"() {
        expect:
        exception(type, parameters as CheckedFunction, args as CheckedFunction)

        where:
        type              | parameters                          | args
        TestException     | null                                | null
        TestException     | parameters(Long.class, Float.class) | null
        TestException     | null                                | arguments(1L, 1.0f)
        AbstractException | null                                | null
    }

    class TestException extends Exception {
        private TestException(String var1) {
            super(var1)
        }
    }

    abstract class AbstractException extends Exception {
        /* Empty */
    }
}
