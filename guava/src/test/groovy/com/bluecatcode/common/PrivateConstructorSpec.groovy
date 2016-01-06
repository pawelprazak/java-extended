package com.bluecatcode.common

import com.bluecatcode.common.base.*
import com.bluecatcode.common.collections.Collections3
import com.bluecatcode.common.concurrent.Futures
import com.bluecatcode.common.concurrent.Try
import com.bluecatcode.common.hash.Hash
import com.bluecatcode.common.io.Files
import com.bluecatcode.common.io.Resources
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

class PrivateConstructorSpec extends Specification {

    @FailsWith(UnsupportedOperationException)
    @Unroll("type: '#type'")
    def "private constructors should be immune to reflection"() {
        expect:
        type.newInstance()

        where:
        type << [
                Collections3,
                Environment,
                Files,
                Futures,
                Hash,
                Postconditions,
                Preconditions,
                Predicates,
                Resources,
                Strings,
                Try
        ]
    }

}
