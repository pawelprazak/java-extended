package com.bluecatcode.fj.test;

import fj.F;
import fj.test.Gen;

import static fj.test.Arbitrary.arbitrary;
import static fj.test.Gen.choose;
import static fj.test.Gen.sized;
import static java.lang.Math.abs;

public class Arbitrary {

    private Arbitrary() {
        throw new UnsupportedOperationException();
    }

    public static fj.test.Arbitrary<Integer> arbIntegerNonNegative() {
        return arbIntegerFrom(0);
    }

    public static fj.test.Arbitrary<Integer> arbIntegerPositive() {
        return arbIntegerFrom(1);
    }

    public static fj.test.Arbitrary<Integer> arbIntegerFrom(final Integer start) {
        return arbitrary(sized(new F<Integer, Gen<Integer>>() {
            @Override
            public Gen<Integer> f(Integer i) {
                return choose(start, start + abs(i));
            }
        }));
    }

    public static fj.test.Arbitrary<Integer> arbIntegerIn(final Integer start, final Integer end) {
        return arbitrary(choose(start, end));
    }

}
