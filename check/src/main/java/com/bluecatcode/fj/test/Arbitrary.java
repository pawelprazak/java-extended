package com.bluecatcode.fj.test;

import fj.F;
import fj.test.Gen;

import static fj.test.Arbitrary.arbitrary;
import static fj.test.Gen.choose;
import static fj.test.Gen.sized;

public class Arbitrary {

    private Arbitrary() {
        throw new UnsupportedOperationException();
    }

    public static fj.test.Arbitrary<Integer> arbIntegerFrom(final Integer start) {
        return arbitrary(sized(new F<Integer, Gen<Integer>>() {
            public Gen<Integer> f(final Integer i) {
                return choose(start, i);
            }
        }));
    }

    public static fj.test.Arbitrary<Integer> arbIntegerIn(final Integer start, final Integer end) {
        return arbitrary(sized(new F<Integer, Gen<Integer>>() {
            public Gen<Integer> f(final Integer i) {
                return choose(start, end);
            }
        }));
    }

}
