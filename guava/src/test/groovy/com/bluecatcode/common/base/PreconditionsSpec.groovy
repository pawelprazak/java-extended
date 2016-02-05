package com.bluecatcode.common.base

import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.collect.FluentIterable
import com.google.common.collect.ImmutableMap
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.base.Preconditions.*
import static com.google.common.collect.Lists.newArrayList
import static com.google.common.collect.Maps.newHashMap

class PreconditionsCheck extends Specification {

    @Unroll("reference: '#reference'")
    def "check should check for the predicate and return reference"() {
        expect:
        reference.is(check(reference, { true } as Predicate))

        where:
        reference << [1, 1L, "1", [], [:]]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "check should check for the predicate and throw"() {
        expect:
        reference.is(check(reference, { false } as Predicate))

        where:
        reference << [1, 1L, "1", [], [:]]
    }

    @FailsWith(NullPointerException)
    @Unroll("reference: '#reference', predicate: '#predicate'")
    def "checkNotEmpty should throw on null"() {
        expect:
        check(reference, predicate as Predicate)

        where:
        reference | predicate
        null      | null
        ""        | null
    }

    @Unroll("'#template', '#args' -> '#result")
    def "messageFromNullable should work regardles of the parameter"() {
        expect:
        result == messageFromNullable(template, args as Object[])
        result == messageFromNullable(template, args as Object[], separator)

        where:
        result | template | args
        "null" | null     | null
        "null" | ""       | null
        "null" | "%s"     | null
        "1"    | null     | "1"
        "1"    | ""       | "1"
        "1"    | "%s"     | "1"

        separator = null
    }
}

class PreconditionsCheckNotEmptySpec extends Specification {

    @Unroll("reference: '#reference' (#reference.class)")
    def "checkNotEmpty should check for emptiness and return the reference"() {
        expect:
        reference.is(checkNotEmpty(reference))
        reference.is(checkNotEmpty(reference, (Object) "test"))
        reference.is(checkNotEmpty(reference, "%s", "test"))

        where:
        reference << [
                " ",
                "some text",
                Optional.of(1),
                [""],
                ["": ""],
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
        checkMatches(reference, pattern, (Object) "test")
        checkMatches(reference, pattern, "%s", "test")

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
                "!test-name#@server-name.com",
                "a" * 63 + "@test.com",
                "test@" + "a" * (254 - 5 - 4) + ".com",
        ]
    }

    @FailsWith(NullPointerException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if null"() {
        expect:
        checkEmail(email)

        where:
        email << [(String) null]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if invalid"() {
        expect:
        checkEmail(email)

        where:
        email << [
                "",
                "@",
                "invalid@server_name.com",
                "a" * 64 + "@test.com",
                "test@" + "a" * 250 + ".com",
        ]
    }

}

class PreconditionsCheckHostnameSpec extends Specification {

    @Unroll("hostname: '#hostname'")
    def "checkEmail should check against RFC 952, RFC 1123 and RFC 1034"() {
        expect:
        checkHostname(hostname)
        checkHostname(hostname, "test")
        checkHostname(hostname, "%s", "test")

        where:
        hostname << [
                "test.pl",
                "server-name.com",
                ("a" * 63 + ".") * 3 + "a" * 59 + ".com",
                "a" * 63 + "." + "a" * 63 + ".com"
        ]
    }

    @FailsWith(NullPointerException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail null -> throws"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [(String) null]
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail '#hostname' -> throws"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [
                "",
                "...",
                "server_name.com",
                ("a" * 63 + ".") * 3 + "a" * 60 + ".com",
                "a" * 64 + ".test.com",
        ]
    }
}

class PreconditionsCheckIsInstanceSpec extends Specification {

    @FailsWith(NullPointerException)
    @Unroll("checkIsInstance '#type.simpleName', '#reference' -> throws NPE")
    def "checkIsInstance should throw if null"() {
        expect:
        checkIsInstance(type, reference)

        where:
        type          | reference
        null as Class | null
        Integer       | null
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("checkIsInstance '#type.simpleName', '#reference' -> throws IAE")
    def "should throw if illegal argument"() {
        expect:
        checkIsInstance(type, reference)

        where:
        type    | reference
        Long    | 1
        Integer | 1L
        String  | Object
    }

    def "checkIsInstance return the same reference"() {
        expect:
        reference.is(checkIsInstance(type, reference))
        reference.is(checkIsInstance(type, reference, "test"))
        reference.is(checkIsInstance(type, reference, "%s", "test"))

        where:
        type    | reference
        Long    | 1L
        Integer | 1
        Object  | String
    }
}