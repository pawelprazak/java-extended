package com.bluecatcode.common.concurrent

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.concurrent.TimeUnit.MILLISECONDS

class SleepSpec extends Specification {

    @Unroll
    def "should sleep incorruptible"() {
        given:
        Sleep.sleepFor(0, MILLISECONDS)
    }

    @Unroll
    def "should sleep unincorruptible"() {
        given:
        Sleep.uninterruptibleSleepFor(0, MILLISECONDS)
    }
}
