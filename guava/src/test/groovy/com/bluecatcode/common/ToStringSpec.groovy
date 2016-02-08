package com.bluecatcode.common

import com.bluecatcode.common.base.Either
import com.bluecatcode.common.io.Closeables
import com.bluecatcode.common.io.Closer
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
        object                                             | _
        Either.valueOf(1)                                  | _
        Either.errorOf(1)                                  | _
        Closeables.closeableFrom("test", { -> } as Closer) | _
    }

}
