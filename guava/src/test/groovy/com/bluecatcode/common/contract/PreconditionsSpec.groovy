package com.bluecatcode.common.contract

import com.bluecatcode.common.contract.errors.RequireViolation
import com.google.common.base.Predicate
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.contract.Preconditions.require

class PreconditionsSpec extends Specification {

    def "ensure returns the reference"() {
        expect:
        require(true)
        require(true, "test")
        require(true, "%s", "test")
        reference.is(require(reference, { true } as Predicate))
        reference.is(require(reference, { true } as Predicate, "test"))
        reference.is(require(reference, { true } as Predicate, "%s", "test"))

        where:
        reference = 1
    }

    @FailsWith(RequireViolation)
    @Unroll
    def "ensure throws on inavlid argument"() {
        expect:
        call()

        where:
        call << [
                { require(false) },
                { require(false, "test") },
                { require(false, "%s", "test") },
                { require(1, { false } as Predicate) },
                { require(1, { false } as Predicate, "test") },
                { require(1, { false } as Predicate, "%s", "test") },
        ]
    }
}