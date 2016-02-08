package com.bluecatcode.common.io

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

class BoundedByteArrayOutputStreamSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll
    def "should throw on invalid arguments"() {
        expect:
        new BoundedByteArrayOutputStream(capacity, limit)

        where:
        capacity | limit
        0        | 1
        1        | 0
    }
}
