package com.bluecatcode.common.base

import com.google.common.base.Optional
import com.google.common.collect.FluentIterable
import com.google.common.collect.ImmutableMap
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Preconditions.*
import static com.google.common.collect.Lists.newArrayList
import static com.google.common.collect.Maps.newHashMap

class PreconditionsCheckNotEmptySpec extends Specification {

    @Unroll("reference: '#reference' (#reference.class)")
    def "checkNotEmpty should check for emptiness and return the reference"() {
        expect:
        reference.is(checkNotEmpty(reference))

        where:
        reference << [
                " ",
                "some text",
                Optional.of(1),
                [""],
                ["":""],
                newArrayList(1, 2, 3),
                ImmutableMap.of("1", "1"),
                FluentIterable.from([""])
        ]
    }

    @FailsWith(NullPointerException)
    @Unroll("reference: '#reference', template: '#template', args: '#args'")
    def "checkNotEmpty should throw on null"() {
        expect:
        checkNotEmpty(reference, template, args)

        where:
        reference | template | args
        null      | null     | null
        ""        | null     | null
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "checkNotEmpty should throw on empty"() {
        expect:
        checkNotEmpty(reference)

        where:
        reference << [
                "", [], [:], Optional.absent(), newArrayList(), newHashMap(), FluentIterable.from([])
        ]
    }
}

class PreconditionsCheckMatchesSpec extends Specification {

    def "checkMatches should pattern match and return the reference"() {
        when:
        checkMatches(reference, pattern)

        then:
        noExceptionThrown()

        where:
        reference | pattern
        "value"   | ~/^v[a-z]*e$/
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "checkMatches should pattern match and throw"() {
        expect:
        checkMatches(reference, pattern)

        where:
        reference | pattern
        "xyz"     | ~/^v[a-z]*e$/
        "x"       | ~/^/
    }
}

class PreconditionsCheckEmailSpec extends Specification {

    @Unroll("email: '#email'")
    def "checkEmail should check against RFC 822"() {
        expect:
        checkEmail(email)

        where:
        email << [
                "test@test.pl",
                "!test-name#@server-name.com"
        ]
    }

    @FailsWith(NullPointerException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if null"() {
        expect:
        checkEmail(email)

        where:
        email << [ (String) null ]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if invalid"() {
        expect:
        checkEmail(email)

        where:
        email << [
                "",
                "invalid@server_name.com"
        ]
    }

}

class PreconditionsCheckHostnameSpec extends Specification {

    @Unroll("hostname: '#hostname'")
    def "checkEmail should check against RFC 952, RFC 1123 and RFC 1034"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [
                "test.pl",
                "server-name.com"
        ]
    }

    @FailsWith(NullPointerException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail should throw if null"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [ (String) null ]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail should throw if invalid"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [
                "",
                "server_name.com"
        ]
    }
}