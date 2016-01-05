package com.bluecatcode.common.concurrent

import com.bluecatcode.common.base.Effect
import com.google.common.util.concurrent.UncheckedTimeoutException
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

import static com.bluecatcode.common.concurrent.Try.tryWith

class TrySpec extends Specification {

    @Unroll("result: '#result'")
    def "should pass the result"() {
        expect:
        result.equals(tryWith({->result}))

        where:
        result << [
            1, "lol", "", null
        ]
    }

    @Unroll("exception: '#exception'")
    @FailsWith(RuntimeException)
    def "should throw on exception with callable"() {
        expect:
        tryWith({->throw exception})

        where:
        exception << [
            new IllegalArgumentException(),
            new IllegalStateException(),
            new IOException()
        ]
    }

    @Unroll("exception: '#exception'")
    @FailsWith(RuntimeException)
    def "should throw on exception with effect"() {
        expect:
        tryWith({->throw exception} as Effect)

        where:
        exception << [
                new IllegalArgumentException(),
                new IllegalStateException(),
                new IOException()
        ]
    }

    @FailsWith(UncheckedTimeoutException)
    def "should throw on timeout"() {
        expect:
        tryWith(1, TimeUnit.MILLISECONDS, {
            sleep(10)
            throw new IOException()
        })
    }

}
