package com.bluecatcode.hamcrest.matchers

import spock.lang.Specification
import spock.lang.Unroll

class CharSequenceSliceMatcherSpec extends Specification {

    @Unroll("slice [#start:#end] of '#sequence' -> '#result'")
    def "test sliceCharSequence"() {
        expect:
        result == CharSequenceSliceMatcher.sliceCharSequence(start, end, sequence)

        where:
        start | end | sequence | result
        0     | 0   | "aaa"    | "a"
        0     | 1   | "aaa"    | "aa"
        0     | 2   | "aaa"    | "aaa"
        0     | -1  | "aaa"    | "aaa"
        0     | -2  | "abc"    | "ab"
        0     | -3  | "abc"    | "a"
        1     | -2  | "abc"    | "b"
        2     | -1  | "abc"    | "c"
        2     | -2  | "abc"    | ""
        1     | -3  | "abc"    | ""
        -3    | -3  | "abc"    | "a"
        -2    | -2  | "abc"    | "b"
        -1    | -1  | "abc"    | "c"
        -3    | -2  | "abc"    | "ab"
        -2    | -1  | "abc"    | "bc"
        -3    | -1  | "abc"    | "abc"
    }
}
