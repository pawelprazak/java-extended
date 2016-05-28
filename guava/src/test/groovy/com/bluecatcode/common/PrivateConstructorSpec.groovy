package com.bluecatcode.common

import com.bluecatcode.common.base.Eithers
import com.bluecatcode.common.base.Environment
import com.bluecatcode.common.base.Strings
import com.bluecatcode.common.collections.Collections3
import com.bluecatcode.common.concurrent.Futures
import com.bluecatcode.common.concurrent.Sleep
import com.bluecatcode.common.concurrent.Try
import com.bluecatcode.common.contract.Checks
import com.bluecatcode.common.contract.Impossibles
import com.bluecatcode.common.contract.Postconditions
import com.bluecatcode.common.contract.Preconditions
import com.bluecatcode.common.exceptions.Exceptions
import com.bluecatcode.common.exceptions.VoidException
import com.bluecatcode.common.hash.Hash
import com.bluecatcode.common.io.Closeables
import com.bluecatcode.common.io.Files
import com.bluecatcode.common.io.Resources
import com.bluecatcode.common.predicates.Predicates
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
        type           | _
        Closeables     | _
        Collections3   | _
        Eithers        | _
        Environment    | _
        Exceptions     | _
        Files          | _
        Futures        | _
        Hash           | _
        Preconditions  | _
        Postconditions | _
        Impossibles    | _
        Checks         | _
        Predicates     | _
        Resources      | _
        Sleep          | _
        Strings        | _
        Try            | _
        VoidException  | _
    }
}
