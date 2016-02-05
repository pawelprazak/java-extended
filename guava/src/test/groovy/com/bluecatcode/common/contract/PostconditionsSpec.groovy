package com.bluecatcode.common.contract

import com.bluecatcode.common.contract.errors.EnsureViolation
import com.google.common.base.Predicate
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.contract.Postconditions.ensure

class PostconditionsSpec extends Specification {

    def "ensure returns the reference"() {
        expect:
        ensure(true)
        ensure(true, "test")
        ensure(true, "%s", "test")
        reference.is(ensure(reference, { true } as Predicate))
        reference.is(ensure(reference, { true } as Predicate, "test"))
        reference.is(ensure(reference, { true } as Predicate, "%s", "test"))

        where:
        reference = 1
    }

    @FailsWith(EnsureViolation)
    @Unroll
    def "ensure throws on inavlid argument"() {
        expect:
        call()

        where:
        call << [
                { ensure(false) },
                { ensure(false, "test") },
                { ensure(false, "%s", "test") },
                { ensure(1, { false } as Predicate) },
                { ensure(1, { false } as Predicate, "test") },
                { ensure(1, { false } as Predicate, "%s", "test") },
        ]
    }
}