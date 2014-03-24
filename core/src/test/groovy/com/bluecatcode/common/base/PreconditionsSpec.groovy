package com.bluecatcode.common.base

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Preconditions.*
import static com.google.common.collect.Lists.newArrayList

class PreconditionsCheckNotEmptySpec extends Specification {

    def "checkNotEmpty should check for emptiness and return the reference"() {
        expect:
        reference.is(checkNotEmpty(reference))

        where:
        reference << [" ", "some text", [""], ["":""], Optional.of(1), newArrayList(1, 2, 3)]
    }

    @FailsWith(NullPointerException)
    @Unroll("reference: '#reference', template: '#template', args: '#args'")
    def "checkNotEmpty should throw on null"() {
        expect:
        checkNotEmpty(reference, template, args)

        where:
        reference | template | args
        null      | null     | null
        ""        | null     | null
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "checkNotEmpty should throw on empty"() {
        expect:
        checkNotEmpty(reference)

        where:
        reference << ["", [], [:], Optional.empty(), newArrayList()]
    }
}

class PreconditionsCheckMatchesSpec extends Specification {

    def "checkMatches should pattern match and return the reference"() {
        when:
        checkMatches(reference, pattern)

        then:
        noExceptionThrown()

        where:
        reference | pattern
        "value"   | ~/^v[a-z]*e$/
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "checkMatches should pattern match and throw"() {
        expect:
        checkMatches(reference, pattern)

        where:
        reference | pattern
        "xyz"     | ~/^v[a-z]*e$/
        "x"       | ~/^/
    }
}