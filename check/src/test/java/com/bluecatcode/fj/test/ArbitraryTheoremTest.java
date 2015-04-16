package com.bluecatcode.fj.test;

import fj.F;
import fj.test.Property;
import fj.test.Rand;
import fj.test.reflect.CheckParams;
import fj.test.reflect.Name;

import static com.bluecatcode.fj.test.Arbitrary.*;
import static fj.test.Arbitrary.arbInteger;
import static fj.test.Property.prop;
import static fj.test.Property.property;

public class ArbitraryTheoremTest extends FunctionalJavaCheck {

    @CheckParams(maxDiscarded = 1000, maxSize = 100000, minSuccessful = 1000)
    @Name("Should produce only integers greater than or equal to 23 and less than or equal to 31337")
    Property generateIntegerIn = property(arbInteger, new F<Integer, Property>() {
        @Override
        public Property f(Integer i) {
            Integer result = arbIntegerIn(23, 31337).gen.gen(i, Rand.standard);
            return prop(result >= 23 && result <= 31337);
        }
    });

    @CheckParams(maxDiscarded = 1000, maxSize = 1000, minSuccessful = 1000)
    @Name("Should produce only integers greater than or equal to 23")
    Property generateIntegerFrom = property(arbInteger, new F<Integer, Property>() {
        @Override
        public Property f(Integer i) {
            Integer result = arbIntegerFrom(23).gen.gen(i, Rand.standard);
            return prop(result >= 23);
        }
    });

    @CheckParams(maxDiscarded = 1000, maxSize = 1000, minSuccessful = 1000)
    @Name("Should produce only positive integers")
    Property generateIntegerPositive = property(arbInteger, new F<Integer, Property>() {
        @Override
        public Property f(Integer i) {
            Integer result = arbIntegerPositive().gen.gen(i, Rand.standard);
            return prop(result > 0);
        }
    });

    @CheckParams(maxDiscarded = 1000, maxSize = 1000, minSuccessful = 1000)
    @Name("Should produce only non-negative integers")
    Property generateIntegerNonNegative = property(arbInteger, new F<Integer, Property>() {
        @Override
        public Property f(Integer i) {
            Integer result = arbIntegerNonNegative().gen.gen(i, Rand.standard);
            return prop(result >= 0);
        }
    });

}