package com.bluecatcode.common.base

import spock.genesis.Gen
import spock.genesis.generators.values.StringGenerator
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Strings.capitalize
import static com.bluecatcode.hamcrest.Matchers.isNotEmptyString
import static com.bluecatcode.hamcrest.Matchers.pattern
import static com.bluecatcode.hamcrest.matchers.CharSequenceSliceMatcher.tailThat
import static org.hamcrest.Matchers.either
import static org.hamcrest.Matchers.is
import static spock.util.matcher.HamcrestSupport.that

class StringsCapitalizeSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    def "should throw on null"() {
        expect:
        capitalize(null)
    }

    @Unroll("string: '#string', result: '#result'")
    def "should capitalize any first lower case letter"() {
        expect:
        result.equals(capitalize(string))

        where:
        string    | result
        ""        | ""
        "A"       | "A"
        "a"       | "A"
        "capital" | "Capital"
        "łąka"    | "Łąka"
    }

    @Unroll
    def "should leave any non-letter and already capitalized starting letter"() {
        expect:
        string.equals(capitalize(string))

        where:
        string << ["", " ", "\n", "\t", "\r", "0", "中国", "TLA"]
    }

    @Unroll
    def "should capitalize any non-empty alphanumeric string"() {
        given:
        def result = capitalize(string as String)

        expect:
        result != null
        that(result, isNotEmptyString())
        that(result, hasFirstLetterCharUpper())
        that(result, hasTheSameTailAs(string))

        where:
        string << anyNonEmptyAlphaStr().take(1000)
    }

    def hasFirstLetterCharUpper() {
        either(pattern(~/^[^\p{javaAlphabetic}].*/)).or(pattern(~/^[\p{javaUpperCase}].*/))
    }

    def hasTheSameTailAs(String string) {
        tailThat(is(string.substring(1)))
    }

    static StringGenerator anyNonEmptyAlphaStr() {
        Gen.string(~/^[\p{L}]+/)
    }
}
