package com.bluecatcode.common.contract

import com.bluecatcode.common.io.Resources
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

    static final def exception = new IllegalArgumentException()
    static final Predicate truePredicate = { true } as Predicate
    static final Predicate falsePredicate = { false } as Predicate

    @Unroll("reference: '#reference'")
    def "check should check for the predicate and return reference"() {
        expect:
        reference.is(check(reference, truePredicate))

        where:
        reference << [1, 1L, "1", [], [:]]
    }

    @Unroll("reference: '#reference'")
    def "check should check for the predicate and return reference or throw custom exception"() {
        expect:
        check(true, new IllegalArgumentException())
        reference.is(check(reference, truePredicate))
        reference.is(check(reference, truePredicate, IllegalArgumentException))
        reference.is(check(reference, truePredicate, IllegalArgumentException, "test"))
        reference.is(check(reference, truePredicate, IllegalArgumentException, "%s", "test"))
        reference.is(check(reference, truePredicate, exception))
        reference.is(check(reference, truePredicate, { exception } as Supplier))

        where:
        reference << [1]
    }


    @FailsWith(IllegalArgumentException)
    @Unroll("#method #args")
    def "check should throw on illegal arguments"() {
        expect:
        method != null
        method.checkParameters(types as Class[])
        method.invoke(Resources, args as Object[])

        where:
        methodName | args                                                        | types
        "check"    | [true, null]                                                | [boolean, Throwable]
        "check"    | [false, exception]                                          | [boolean, Throwable]

        "check"    | [null, null]                                                | [Object, Predicate]
        "check"    | [1, null]                                                   | [Object, Predicate]
        "check"    | [1, falsePredicate]                                         | [Object, Predicate]
        "check"    | [1, falsePredicate, "test"]                                 | [Object, Predicate, Object]
        "check"    | [1, falsePredicate, "%s", "test"]                           | [Object, Predicate, String, Object[]]

        "check"    | [null, null, null]                                          | [Object, Predicate, Class]
        "check"    | [1, null, null]                                             | [Object, Predicate, Class]
        "check"    | [1, truePredicate, null]                                    | [Object, Predicate, Class]
        "check"    | [1, falsePredicate, IllegalArgumentException]               | [Object, Predicate, Class]
        "check"    | [1, falsePredicate, IllegalArgumentException, "test"]       | [Object, Predicate, Class, String]
        "check"    | [1, falsePredicate, IllegalArgumentException, "%s", "test"] | [Object, Predicate, Class, String, Object[]]

        "check"    | [null, null, null]                                          | [Object, Predicate, Throwable]
        "check"    | [1, null, null]                                             | [Object, Predicate, Throwable]
        "check"    | [1, truePredicate, null]                                    | [Object, Predicate, Throwable]
        "check"    | [1, falsePredicate, exception]                              | [Object, Predicate, Throwable]

        "check"    | [null, null, null]                                          | [Object, Predicate, Supplier]
        "check"    | [1, null, null]                                             | [Object, Predicate, Supplier]
        "check"    | [1, truePredicate, null]                                    | [Object, Predicate, Supplier]
        "check"    | [1, falsePredicate, { exception } as Supplier]              | [Object, Predicate, Supplier]

        method = Checks.metaClass.pickMethod(methodName, types as Class[])
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
        reference                 | _
        " "                       | _
        "some text"               | _
        Optional.of(1)            | _
        [""]                      | _
        ["": ""]                  | _
        newArrayList(1, 2, 3)     | _
        ImmutableMap.of("1", "1") | _
        FluentIterable.from([""]) | _
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
        null      | ~/.*/
        "a"       | null
        ""        | ~/ /
    }
}

class CheckEmailSpec extends Specification {

    @Unroll("email: '#email'")
    def "checkEmail should check against RFC 822"() {
        expect:
        checkEmail(email)

        where:
        email                                  | _
        "test@test.pl"                         | _
        "!test-name#@server-name.com"          | _
        "a" * 63 + "@test.com"                 | _
        "test@" + "a" * (254 - 5 - 4) + ".com" | _
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("email: '#email'")
    def "checkEmail should throw if invalid"() {
        expect:
        checkEmail(email)

        where:
        email                        | _
        null                         | _
        ""                           | _
        "@"                          | _
        "test@"                      | _
        "@test"                      | _
        "invalid@server_name.com"    | _
        "a" * 64 + "@test.com"       | _
        "test@" + "a" * 250 + ".com" | _
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
        hostname                                 | _
        "test.pl"                                | _
        "server-name.com"                        | _
        "server-name.com"                        | _
        ("a" * 63 + ".") * 3 + "a" * 59 + ".com" | _
        "a" * 63 + "." + "a" * 63 + ".com"       | _
    }

    @FailsWith(IllegalArgumentException)
    @Unroll("hostname: '#hostname'")
    def "checkEmail '#hostname' -> throws"() {
        expect:
        checkHostname(hostname)

        where:
        hostname                                 | _
        null                                     | _
        ""                                       | _
        "..."                                    | _
        ".com"                                   | _
        "server_name.com"                        | _
        ("a" * 63 + ".") * 3 + "a" * 60 + ".com" | _
        "a" * 64 + ".test.com"                   | _
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
    @Unroll("checkUri '#reference' -> throws IAE")
    def "should throw if illegal argument"() {
        expect:
        checkUri(reference)
        checkUri(reference, (Object) "test")
        checkUri(reference, "%s", "test")

        where:
        reference      | _
        null as String | _
        ""             | _
        "a" * 2000     | _
    }

    def "checkUri return the same reference"() {
        expect:
        reference.is(checkUri(reference))
        reference.is(checkUri(reference, (Object) "test"))
        reference.is(checkUri(reference, "%s", "test"))

        where:
        reference           | _
        "file:/tmp/example" | _
    }
}