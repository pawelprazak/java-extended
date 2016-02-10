package com.bluecatcode.time

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

class PrivateConstructorsSpec extends Specification {
    @FailsWith(UnsupportedOperationException)
    @Unroll("type: '#type'")
    def "private constructors should be immune to reflection"() {
        expect:
        type.newInstance()

        where:
        type         | _
        Milliseconds | _
        ISO8601      | _
    }
}
