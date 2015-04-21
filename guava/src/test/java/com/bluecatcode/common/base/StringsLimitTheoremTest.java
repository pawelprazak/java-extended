package com.bluecatcode.common.base;

import com.bluecatcode.fj.test.FunctionalJavaCheck;
import fj.F2;
import fj.test.Property;
import fj.test.reflect.CheckParams;
import fj.test.reflect.Name;

import static com.bluecatcode.fj.test.Arbitrary.arbIntegerFrom;
import static com.bluecatcode.fj.test.Arbitrary.arbIntegerIn;
import static fj.test.Arbitrary.arbString;
import static fj.test.Bool.bool;
import static fj.test.Property.property;

public class StringsLimitTheoremTest extends FunctionalJavaCheck {

    @Name("Must add ellipsis when string length is > max value and max value is > 4")
    Property addsEllipsis = property(arbString, arbIntegerFrom(4), new F2<String, Integer, Property>() {
        @Override
        public Property f(String s, Integer max) {
            String result = Strings.limitCharacters(s, max);
            return bool(s.length() > max).implies(result.endsWith("..."));
        }
    });

    @CheckParams(maxDiscarded = 1000)
    @Name("Should not add ellipsis when string length is <= max value and max value is in 0..3")
    Property addsNoEllipsisIfShort = property(arbString, arbIntegerIn(0, 3), new F2<String, Integer, Property>() {
        @Override
        public Property f(String s, Integer max) {
            String result = Strings.limitCharacters(s, max);
            return bool(s.length() <= max).implies(!result.endsWith("..."));
        }
    });

    @Name("Must leave string as is when length <= max value")
    Property staysTheSame = property(arbString, arbIntegerFrom(0), new F2<String, Integer, Property>() {
        @Override
        public Property f(String s, Integer max) {
            String result = Strings.limitCharacters(s, max);
            return bool(s.length() <= max).implies(s.contentEquals(result));
        }
    });

}