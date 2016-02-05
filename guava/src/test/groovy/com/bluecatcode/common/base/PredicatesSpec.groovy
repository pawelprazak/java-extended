package com.bluecatcode.common.base

import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.collect.FluentIterable
import com.google.common.collect.ImmutableMap
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.google.common.collect.Lists.newArrayList
import static com.google.common.collect.Maps.newHashMap

class PredicatesSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("method (null) -> IllegalArgumentException")
    def "should throw on null argument"() {
        expect:
        method(null)

        where:
        method << [
                Predicates.&isInstance,
        ]
    }

    @Unroll("#method (null) -> false")
    def "should return false on null argument"() {
        expect:
        result == method(null)

        where:
        result | method
        false  | Predicates.isValidURI().&apply
        false  | Predicates.isValidEmail().&apply
        false  | Predicates.isInstance(Long as Class[]).&apply
    }

    @Unroll("expected: #expected when '#argument' (#argType) and '#types'")
    def "should work with any non-null argument"() {
        given:
        def predicate = Predicates.&isInstance(types as Class[])

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

class PredicatesLogicSpec extends Specification {

    @Unroll("A: #a, B: #b -> Q: #result")
    def "should perform NOR"() {
        given:
        def q = Predicates.nor({ _ -> a } as Predicate, { _ -> b } as Predicate)

        expect:
        q.apply("any") == result

        where:
        result | a     | b
        true   | false | false
        false  | false | true
        false  | true  | false
        false  | true  | true
    }

    @Unroll("A: #a, B: #b, C: #c -> Q: #result")
    def "should perform NOR(3)"() {
        given:
        def q = Predicates.nor({ _ -> a } as Predicate, { _ -> b } as Predicate, { _ -> c } as Predicate)

        expect:
        q.apply("any") == result

        where:
        result | a     | b     | c
        true   | false | false | false
        false  | false | false | true
        false  | false | true  | false
        false  | false | true  | true
        false  | true  | false | false
        false  | true  | false | true
        false  | true  | true  | false
        false  | true  | true  | true
    }

    @Unroll("A: #a, B: #b -> Q: #result")
    def "should perform NAND"() {
        given:
        def q = Predicates.nand({ _ -> a } as Predicate, { _ -> b } as Predicate)

        expect:
        q.apply("any") == result

        where:
        result | a     | b
        true   | false | false
        true   | false | true
        true   | true  | false
        false  | true  | true
    }

    @Unroll("A: #a, B: #b, C: #c -> Q: #result")
    def "should perform NAND(3)"() {
        given:
        def q = Predicates.nand({ _ -> a } as Predicate, { _ -> b } as Predicate, { _ -> c } as Predicate)

        expect:
        q.apply("any") == result

        where:
        result | a     | b     | c
        true   | false | false | false
        true   | false | false | true
        true   | false | true  | false
        true   | false | true  | true
        true   | true  | false | false
        true   | true  | false | true
        true   | true  | true  | false
        false  | true  | true  | true
    }
}

class PredicatesIsEmptySpec extends Specification {

    @Unroll
    def "isEmptyObject should work with: #reference"() {
        setup:
        def isEmptyPredicate = Predicates.isEmptyObject()

        when:
        def result = isEmptyPredicate.apply(reference)

        then:
        assert result == expected
        noExceptionThrown()

        where:
        reference                              | expected
        null                                   | true
        ""                                     | true
        new StringBuilder("")                  | true
        Optional.absent()                      | true
        []                                     | true
        [:]                                    | true
        newArrayList()                         | true
        newHashMap()                           | true
        hashtable()                            | true
        FluentIterable.from([])                | true
        [isEmpty: { return true }] as IsEmpty  | true
        [:] as Map                             | true
        [] as Object[]                         | true
        [] as boolean[]                        | true
        [] as byte[]                           | true
        [] as short[]                          | true
        [] as char[]                           | true
        [] as int[]                            | true
        [] as long[]                           | true
        [] as float[]                          | true
        [] as double[]                         | true

        " "                                    | false
        "value"                                | false
        new StringBuilder("value")             | false
        Optional.of(1)                         | false
        [null]                                 | false
        [""]                                   | false
        ["": ""]                               | false
        newArrayList(1, 2, 3)                  | false
        ImmutableMap.of("1", "1")              | false
        hashtable(["1": "value"])              | false
        FluentIterable.from([""])              | false
        [isEmpty: { return false }] as IsEmpty | false
        [1, 2] as Object[]                     | false
        [false, true] as boolean[]             | false
        [1, 2] as byte[]                       | false
        [1, 2] as short[]                      | false
        [1, 2] as char[]                       | false
        [1, 2] as int[]                        | false
        [1, 2] as long[]                       | false
        [1, 2] as float[]                      | false
        [1, 2] as double[]                     | false
    }

    @FailsWith(IllegalArgumentException)
    @Unroll
    def "isEmptyObject should fail with: #reference"() {
        expect:
        Predicates.isEmptyObject().apply(reference)

        where:
        reference << [
                new Object()
        ]
    }

    def Dictionary hashtable(Map map = [:]) {
        def hashtable = new Hashtable()
        hashtable.putAll(map)
        return hashtable;
    }
}
