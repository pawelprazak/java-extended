package com.bluecatcode.common.predicates

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

class IsInstancePredicateTest extends Specification {

    @FailsWith(IllegalArgumentException)
    def "should throw on null argument"() {
        expect:
        IsInstancePredicate.&isInstancePredicate(null)
    }

    @Unroll("expected: #expected when '#argument' (#argType) and '#types'")
    def "should work with any non-null argument"() {
        given:
        def predicate = IsInstancePredicate.&isInstancePredicate(types as Class[])

        expect:
        predicate.apply(argument).equals(expected)

        where:
        expected | argument | types
        true     | 1        | Integer
        true     | 1L       | [Integer, Long, Float, Double]
        true     | 1.0f     | [Integer, Long, Float, Double]
        false    | null     | Integer
        false    | 1.0      | [Integer, Long]

        argType = argument == null ? "null" : argument.class.simpleName
    }
}
