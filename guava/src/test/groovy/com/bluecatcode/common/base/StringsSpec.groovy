package com.bluecatcode.common.base

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Strings.capitalize

class StringsCapitalizeSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    def "should throw on null"() {
        expect:
        capitalize(null)
    }

    @Unroll("string: '#string', result: '#result'")
    def "should pass"() {
        expect:
        result.equals(capitalize(string))

        where:
        string       | result
        ""           | ""
        "A"          | "A"
        "a"          | "A"
        "capital"    | "Capital"
    }

}
