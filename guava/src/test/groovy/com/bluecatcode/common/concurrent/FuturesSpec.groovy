package com.bluecatcode.common.concurrent

import com.google.common.base.Function
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.ListeningExecutorService
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.concurrent.Futures.futureWith

class FuturesSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("futureWith #executor, #task, #callback -> throws IAE")
    def "should throw on invalid arguments"() {
        expect:
        futureWith(executor, task, callback)

        where:
        executor                       | task                 | callback
        null                           | { true } as Function | { true } as FutureCallback
        Mock(ListeningExecutorService) | null                 | { true } as FutureCallback
        Mock(ListeningExecutorService) | { true } as Function | null
    }
}