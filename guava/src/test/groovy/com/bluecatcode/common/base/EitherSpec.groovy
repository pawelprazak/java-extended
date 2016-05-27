package com.bluecatcode.common.base

import com.bluecatcode.common.contract.errors.ContractViolation
import com.bluecatcode.common.contract.errors.RequireViolation
import com.google.common.base.Function
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Either.*

/**
 * @see EitherTest
 */
class EitherSpec extends Specification {

    @FailsWith(RequireViolation)
    @Unroll("#type #methodName -> throw on null argument")
    def "should throw on null argument"() {
        expect:
        reference."${methodName}"(null)

        where:
        methodName | reference
        "valueOf"  | Either
        "errorOf"  | Either
        "leftOf"   | Either
        "rightOf"  | Either
        "or"       | rightOf(1)
        "or"       | leftOf(1)
        "orThrow"  | rightOf(1)
        "orThrow"  | leftOf(1)

        type = reference instanceof Class ? reference.simpleName : reference.class.simpleName
    }

    @FailsWith(RequireViolation)
    @Unroll("#type #methodName -> throw on null arguments")
    def "should throw on null argumenst"() {
        expect:
        reference."${methodName}"(arg1, arg2)

        where:
        methodName  | reference  | arg1 | arg2
        "either"    | rightOf(1) | null | null
        "either"    | leftOf(1)  | null | null
        "transform" | rightOf(1) | null | null
        "transform" | leftOf(1)  | null | null

        type = reference instanceof Class ? reference.simpleName : reference.class.simpleName
    }

    def "should work with the left"() {
        expect:
        //noinspection GroovyAssignabilityCheck
        def either = leftOf(value)
        expected.equals(either.left())
        expected.equals(either.error())
        either.isLeft()
        either.isError()
        !either.isRight()
        !either.isValue()

        where:
        value | expected
        "a"   | "a"
        123   | 123
    }

    def "should work with the right"() {
        given:
        //noinspection GroovyAssignabilityCheck
        def either = rightOf(value)

        expect:
        expected.equals(either.right())
        expected.equals(either.value())
        !either.isLeft()
        !either.isError()
        either.isRight()
        either.isValue()

        where:
        value | expected
        "a"   | "a"
        123   | 123
    }

    @FailsWith(ContractViolation)
    @Unroll("#reference.class.simpleName #methodName -> should throw")
    def "should throw on invalid state"() {
        expect:
        reference."${methodName}"()

        where:
        methodName | reference
        "left"     | rightOf(1)
        "right"    | leftOf(1)
    }

    @Unroll("#reference.class.simpleName swap -> #type.simpleName ")
    def "should swap on swap()"() {
        given:
        def result = reference.swap()

        expect:
        result.class.equals(type)

        where:
        type  | reference
        Left  | rightOf(1)
        Right | leftOf(1)
    }

    @Unroll("#reference.class.simpleName or -> #expected ")
    def "should choose either right or second choice"() {
        given:
        def result = reference.or(valueOf(2)).right()

        expect:
        result == expected

        where:
        expected | reference
        1        | rightOf(1)
        2        | leftOf(1)
    }

    def "should choose either left or right function result"() {
        given:
        def result = reference.either({ o -> "left" } as Function, { o -> "right" } as Function)

        expect:
        result.equals(expected)

        where:
        expected | reference
        "left"   | leftOf(0)
        "right"  | rightOf(0)
    }

    def "should transform left or right function result"() {
        given:
        def result = reference.transform({ o -> "left" } as Function, { o -> "right" } as Function)
        def value = result.class.equals(Left) ? result.left() : result.right()

        expect:
        value.equals(expected)

        where:
        expected | reference
        "left"   | leftOf(0)
        "right"  | rightOf(0)
    }
}
