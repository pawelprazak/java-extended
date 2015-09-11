package com.bluecatcode.common.base

import com.bluecatcode.common.interfaces.IsEmpty
import com.google.common.base.Optional
import com.google.common.collect.FluentIterable
import com.google.common.collect.ImmutableMap
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.google.common.collect.Lists.newArrayList
import static com.google.common.collect.Maps.newHashMap

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
        Optional.absent()                      | true
        []                                     | true
        [:]                                    | true
        newArrayList()                         | true
        newHashMap()                           | true
        new Hashtable()                        | true
        FluentIterable.from([])                | true
        [isEmpty: { return true }] as IsEmpty  | true
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
        Optional.of(1)                         | false
        [null]                                 | false
        [""]                                   | false
        ["": ""]                               | false
        newArrayList(1, 2, 3)                  | false
        ImmutableMap.of("1", "1")              | false
        FluentIterable.from([""])              | false
        [isEmpty: { return false }] as IsEmpty | false
        [false, true] as boolean[]             | false
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
}
