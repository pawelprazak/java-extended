package com.bluecatcode.common.base

import spock.lang.FailsWith
import spock.lang.Specification


class ExceptionSupplierTest extends Specification {

    @FailsWith(IllegalArgumentException)
    def "should throw an exception"() {
        given:
        def supplier = ExceptionSupplier.throwA(String, new IllegalArgumentException())

        expect:
        supplier.get()
    }
}
