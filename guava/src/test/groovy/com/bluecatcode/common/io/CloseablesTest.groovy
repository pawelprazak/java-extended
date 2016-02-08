package com.bluecatcode.common.io

import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.*

import static com.bluecatcode.common.io.Closeables.closeQuietly
import static com.bluecatcode.common.io.Closeables.closeableFrom

class CloseablesSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("#methodName #args #types -> IAE")
    def "should throw on invalid arguments"() {
        expect:
        method.invoke(Resources, args as Object[])

        where:
        methodName      | args                | types
        "closeableFrom" | null                | Connection
        "closeableFrom" | null                | Statement
        "closeableFrom" | null                | Clob
        "closeableFrom" | [null, null]        | [Array, Closer]
        "closeableFrom" | [Mock(Array), null] | [Array, Closer]

        method = Closeables.metaClass.pickMethod(methodName, types as Class[])
    }

    @Unroll("#methodName #args #types -> Closeable")
    def "should create Closeable"() {
        expect:
        method != null
        method.checkParameters(types as Class[])
        def result = method.invoke(Resources, args as Object[]) as CloseableReference
        result instanceof Closeable
        result.get()

        where:
        methodName      | args                                             | types
        "closeableFrom" | Mock(Connection)                                 | Connection
        "closeableFrom" | Mock(Statement)                                  | Statement
        "closeableFrom" | Mock(Clob)                                       | Clob
        "closeableFrom" | Mock(Blob)                                       | Blob
        "closeableFrom" | [Mock(Array), { Array a -> a.free() } as Closer] | [Array, Closer]

        method = Closeables.metaClass.pickMethod(methodName, types as Class[])
    }

    def "should invoke the closer"() {
        given:
        def connection = Mock(Connection)
        def closer = { c -> c.close() } as Closer

        when:
        closeableFrom(connection, closer).close()

        then:
        1 * connection.close()
        0 * _
    }

    def "should propagate the closer exception"() {
        given:
        def connection = Mock(Connection)
        connection.close() >> { throw new RuntimeException("test") }

        def closer = { c -> c.close() } as Closer

        when:
        closeableFrom(connection, closer).close()

        then:
        def throwable = thrown(IOException)
        throwable.cause instanceof RuntimeException
    }

    def "should check for null reference state at close"() {
        given:
        def connection = Mock(Connection)

        def closer = { c -> c.close() } as Closer
        def reference = closeableFrom(connection, closer)
        reference.close()

        when:
        reference.get()

        then:
        thrown IllegalStateException
    }

    def "should close quietly"() {
        given:
        def connection = Mock(Connection)
        connection.close() >> { throw new RuntimeException("test") }
        def closer = { c -> c.close() } as Closer

        when:
        closeQuietly(closeableFrom(connection, closer))

        then:
        noExceptionThrown()
    }
}
