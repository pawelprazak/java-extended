package com.bluecatcode.common

import com.bluecatcode.common.base.Environment
import com.bluecatcode.common.base.Exceptions
import com.bluecatcode.common.base.Predicates
import com.bluecatcode.common.base.Strings
import com.bluecatcode.common.collections.Collections3
import com.bluecatcode.common.concurrent.Futures
import com.bluecatcode.common.concurrent.Sleep
import com.bluecatcode.common.concurrent.Try
import com.bluecatcode.common.contract.Checks
import com.bluecatcode.common.contract.Impossibles
import com.bluecatcode.common.contract.Postconditions
import com.bluecatcode.common.contract.Preconditions
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
                Exceptions,
                Files,
                Futures,
                Hash,
                Preconditions,
                Postconditions,
                Impossibles,
                Checks,
                Predicates,
                Resources,
                Sleep,
                Strings,
                Try
        ]
    }

}
