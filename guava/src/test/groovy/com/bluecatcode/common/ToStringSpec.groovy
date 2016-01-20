package com.bluecatcode.common

import com.bluecatcode.common.base.Either
import spock.lang.Specification
import spock.lang.Unroll

class ToStringSpec extends Specification {

    @Unroll("object: '#object'")
    def "should have a custom toString()"() {
        given:
        def result = object.toString()

        expect:
        !result.isEmpty()
        !result.startsWith("java.lang.Object@")

        where:
        object << [
                Either.valueOf(1),
                Either.errorOf(1),
        ]
    }

}
