package com.bluecatcode.common.base;

import com.bluecatcode.fj.test.FunctionalJavaCheck;
import fj.F;
import fj.test.Property;
import fj.test.reflect.CheckParams;
import fj.test.reflect.Name;
import org.junit.Test;

import static com.bluecatcode.common.base.Strings.capitalize;
import static fj.test.Arbitrary.arbAlphaNumString;
import static fj.test.Arbitrary.arbitrary;
import static fj.test.Bool.bool;
import static fj.test.Gen.elements;
import static fj.test.Property.prop;
import static fj.test.Property.property;
import static java.lang.Character.isLetter;
import static java.lang.Character.isUpperCase;

public class StringsCapitalizeTheoremTest extends FunctionalJavaCheck {

    @CheckParams(maxDiscarded = 1000, minSuccessful = 1000, maxSize = 100)
    @Name("Should capitalize any string")
    Property capitalizeFirst = property(arbAlphaNumString, new F<String, Property>() {
        @Override
        public Property f(String s) {
            String result = capitalize(s);
            return bool(startsWithLetter(result)).implies(startsWithUpperCase(result));
        }
    });

    @CheckParams(maxDiscarded = 1000, minSuccessful = 10, maxSize = 100)
    @Name("Should leave any non-letter starting string")
    Property capitalizeEmpty = property(arbitrary(elements("", " ", "\n", "\t", "\r", "0", "中国")), new F<String, Property>() {
        @Override
        public Property f(String s) {
            return prop(capitalize(s).equals(s));
        }
    });

    private boolean startsWithLetter(String s) {
        return !s.isEmpty() && isLetter(s.charAt(0));
    }

    private boolean startsWithUpperCase(String s) {
        return !s.isEmpty() && isUpperCase(s.charAt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleNull() throws Exception {
        // given
        String s = null;

        // when
        //noinspection ConstantConditions
        capitalize(s);
    }

}