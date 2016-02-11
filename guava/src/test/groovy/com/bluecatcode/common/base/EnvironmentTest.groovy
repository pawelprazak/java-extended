package com.bluecatcode.common.base

import com.bluecatcode.junit.rules.AssumeMore;
import com.google.common.base.Optional
import spock.lang.Specification

class EnvironmentTest extends Specification {

    def "should provide local host name"() {
        when:
        Optional<String> name = Environment.getLocalHostName()

        then:
        name.isPresent()
        !name.get().isEmpty()
    }

    def "should get an environment variable"() {
        given:
        assumption()

        and:
        def result = Environment.getEnvironmentVariable(name)

        expect:
        result != null
		result.isPresent()
		!result.get().isEmpty()
        predicate(result.get())

        where:
        name   | assumption                  | predicate
        "PATH" | AssumeMore.&assumeIsUnix    | { value -> value.contains("/bin") }
        "TEMP" | AssumeMore.&assumeIsWindows | { value -> value.startsWith("C:") }
    }

    def "should get a system property"() {
        given:
        def result = Environment.getSystemProperty(name);

        expect:
		result != null
		result.isPresent()
		!result.get().isEmpty()
        predicate(result.get())

        where:
        name | predicate
        "java.home" | { value -> value.contains("jdk") }
    }

    def "should get next available port"() {
        given:
        def port = Environment.getNextAvailablePort(fromPort, toPort)

        expect:
        port > 0

        where:
        fromPort | toPort
        1        | 1024
    }
}
