package com.bluecatcode.common.contract

import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.base.Supplier
import com.google.common.collect.FluentIterable
import com.google.common.collect.ImmutableMap
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import static com.bluecatcode.common.contract.Checks.*
import static com.google.common.collect.Lists.newArrayList
import static com.google.common.collect.Maps.newHashMap

class CheckSpec extends Specification {

    @Unroll("reference: '#reference'")
    def "check should check for the predicate and return reference"() {
        expect:
        reference.is(check(reference, { true } as Predicate))

        where:
        reference << [1, 1L, "1", [], [:]]
    }

    @Unroll("reference: '#reference'")
    def "check should check for the predicate and return reference or throw custom exception"() {
        expect:
        check(true, new IllegalArgumentException())
        reference.is(check(reference, { true } as Predicate))
        reference.is(check(reference, { true } as Predicate, IllegalArgumentException))
        reference.is(check(reference, { true } as Predicate, IllegalArgumentException, "test"))
        reference.is(check(reference, { true } as Predicate, IllegalArgumentException, "%s", "test"))
        reference.is(check(reference, { true } as Predicate, new IllegalArgumentException()))
        reference.is(check(reference, { true } as Predicate, { new IllegalArgumentException() } as Supplier))

        where:
        reference << [1, 1L, "1", [], [:]]
    }


    @FailsWith(IllegalArgumentException)
    @Unroll("reference: '#reference'")
    def "check should check for the predicate and throw"() {
        expect:
        check(false, new IllegalArgumentException())
        check(reference, { false } as Predicate)
        check(reference, { false } as Predicate, IllegalArgumentException)
        check(reference, { false } as Predicate, IllegalArgumentException, "test")
        check(reference, { false } as Predicate, IllegalArgumentException, "%s", "test")
        check(reference, { false } as Predicate, new IllegalArgumentException())
        check(reference, { false } as Predicate, { new IllegalArgumentException() } as Supplier)
        check(true, (Throwable) null)
        check(null, (Predicate) null)
        check(reference, (Predicate) null)
        check(reference, { true } as Predicate, (Class) null)
        check(reference, { true } as Predicate, (Throwable) null)
        check(reference, { true } as Predicate, (Supplier) null)
        check(reference, { true } as Predicate, { null } as Supplier)

        where:
        reference = 1
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

class CheckNotEmptySpec extends Specification {

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

    @FailsWith(IllegalArgumentException)
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

class CheckMatchesSpec extends Specification {

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

class CheckEmailSpec extends Specification {

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

    @FailsWith(IllegalArgumentException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if invalid"() {
        expect:
        checkEmail(email)

        where:
        email << [
                (String) null,
                "",
                "@",
                "invalid@server_name.com",
                "a" * 64 + "@test.com",
                "test@" + "a" * 250 + ".com",
        ]
    }

}

class CheckHostnameSpec extends Specification {

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

    @FailsWith(IllegalArgumentException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail '#hostname' -> throws"() {
        expect:
        checkHostname(hostname)

        where:
        hostname << [
                (String) null,
                "",
                "...",
                "server_name.com",
                ("a" * 63 + ".") * 3 + "a" * 60 + ".com",
                "a" * 64 + ".test.com",
        ]
    }
}

class CheckIsInstanceSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("checkIsInstance '#type.simpleName', '#reference' -> throws IAE")
    def "should throw if illegal argument"() {
        expect:
        checkIsInstance(type, reference)

        where:
        type          | reference
        null as Class | null
        Integer       | null
        Long          | 1
        Integer       | 1L
        String        | Object
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

class CheckUriSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("checkUri'#reference' -> throws IAE")
    def "should throw if illegal argument"() {
        expect:
        checkUri(reference)
        checkUri(reference, (Object) "test")
        checkUri(reference, "%s", "test")

        where:
        reference << [
                (String) null,
                "",
                "a" * 2000
        ]
    }

    def "checkUri return the same reference"() {
        expect:
        reference.is(checkUri(reference))
        reference.is(checkUri(reference, (Object) "test"))
        reference.is(checkUri(reference, "%s", "test"))

        where:
        reference << [
                "file:/tmp/example"
        ]
    }
}