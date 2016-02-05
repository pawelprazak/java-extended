package com.bluecatcode.common.contract

import com.bluecatcode.common.contract.errors.ImpossibleViolation
import spock.lang.FailsWith
import spock.lang.Specification

import static com.bluecatcode.common.contract.Impossibles.impossible

class ImpossiblesSpec extends Specification {

    @FailsWith(ImpossibleViolation)
    def "should throw unconditionally"() {
        expect:
        call()

        where:
        call << [
                { impossible() },
                { impossible("test") },
                { impossible("%s", "test") },
        ]
    }
}