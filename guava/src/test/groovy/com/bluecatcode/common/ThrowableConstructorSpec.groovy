package com.bluecatcode.common

import com.bluecatcode.common.contract.errors.ContractViolation
import com.bluecatcode.common.contract.errors.EnsureViolation
import com.bluecatcode.common.contract.errors.ImpossibleViolation
import com.bluecatcode.common.contract.errors.RequireViolation
import com.bluecatcode.common.exceptions.CheckedException
import com.bluecatcode.common.exceptions.UncheckedException
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Exceptions.*

class ThrowableConstructorSpec extends Specification {

    @Unroll("#type.simpleName '#message' '#cause'")
    def "throwable constructors should have basic costructors"() {
        given:
        def throwable0 = type.newInstance()
        def throwable1 = throwable(type, parameters(String.class), arguments(message))
        def throwable2 = throwable(type, parameters(String.class, Throwable.class), arguments(message, cause))
        def throwable1t = throwable(type, parameters(Throwable.class), arguments(cause))

        expect:
        throwable0 != null
        throwable1 != null
        throwable2 != null
        throwable1t != null
        throwable0.class == type
        throwable1.class == type
        throwable2.class == type
        throwable1t.class == type
        throwable1.message == message
        throwable2.message == message
        throwable2.cause == cause
        throwable1t.cause == cause

        where:
        type                | message        | cause
        ContractViolation   | null           | null
        ContractViolation   | "test message" | null
        ContractViolation   | "test message" | new IllegalArgumentException()
        RequireViolation    | null           | null
        RequireViolation    | "test message" | null
        RequireViolation    | "test message" | new IllegalArgumentException()
        EnsureViolation     | null           | null
        EnsureViolation     | "test message" | null
        EnsureViolation     | "test message" | new IllegalArgumentException()
        ImpossibleViolation | null           | null
        ImpossibleViolation | "test message" | null
        ImpossibleViolation | "test message" | new IllegalArgumentException()
        CheckedException    | null           | null
        CheckedException    | "test message" | null
        CheckedException    | "test message" | new IllegalArgumentException()
        UncheckedException  | null           | null
        UncheckedException  | "test message" | null
        UncheckedException  | "test message" | new IllegalArgumentException()
    }

}
