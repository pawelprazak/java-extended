package com.bluecatcode.common.collections

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @see Collections3Test
 */
class Collections3Spec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("#methodName -> throw on null argument")
    def "should throw on null argument"() {
        expect:
        Collections3."${methodName}"(null)

        where:
        methodName << ["fromDictionary"]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("#methodName -> throw on null arguments")
    def "should throw on null argumenst"() {
        expect:
        Collections3."${methodName}"(arg1, arg2)

        where:
        methodName       | arg1             | arg2
        "zip"            | null             | null
        "zip"            | [] as List       | null
        "fromDictionary" | null             | null
        "fromDictionary" | {} as Dictionary | null
    }
}
