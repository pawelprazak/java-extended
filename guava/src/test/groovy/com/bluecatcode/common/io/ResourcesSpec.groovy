package com.bluecatcode.common.io

import com.google.common.io.LineProcessor
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

class ResourcesSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    @Unroll("#method #args")
    def "should throw on illegal arguments"() {
        expect:
        method != null
        method.checkParameters(types as Class[])
        method.invoke(Resources, args as Object[])

        where:
        methodName            | args                          | types
        "getResourceAsString" | [null, null]                  | [ClassLoader, String]
        "getResourceAsString" | [Resources.classLoader, null] | [ClassLoader, String]
        "getResourceAsString" | [Resources.classLoader, ""]   | [ClassLoader, String]
        "getResourceAsString" | [null, null]                  | [Class, String]
        "getResourceAsString" | [Resources, null]             | [Class, String]
        "getResourceAsString" | [Resources, ""]               | [Class, String]
        "getResourceWith"     | [null, null, null]            | [Class, String, LineProcessor]
        "getResourceWith"     | [Resources, null, null]       | [Class, String, LineProcessor]
        "getResourceWith"     | [Resources, "", null]         | [Class, String, LineProcessor]
        "getResourceWith"     | [Resources, "name", null]     | [Class, String, LineProcessor]
        "getResourceAsStream" | [null, null]                  | [Class, String]
        "getResourceAsStream" | [Resources, null]             | [Class, String]
        "getResourceAsStream" | [Resources, ""]               | [Class, String]

        method = Resources.metaClass.pickMethod(methodName, types as Class[])
    }
}