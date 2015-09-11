package com.bluecatcode.common.base;

import com.google.auto.value.AutoValue;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;

import static com.bluecatcode.common.base.StringsCountTest.Data.data;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Theories.class)
public class StringsCountTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @DataPoints
    public static final Data[] samples = new Data[] {
            data(null, null, 0),
            data("", null, 0),
            data("", "", 0),
            data(null, "a", 0),
            data("a", "aa", 0),
            data("", ".", 0),
            data("", "..", 0),
            data("", "a", 0),
            data("a", "a", 1),
            data("aaa", "a", 3),
            data("....", ".", 4),
            data("/../../../", ".", 6),
            data("/../../../", "/", 4),
            data("aaaaaa", "aa", 3),
            data("....", "..", 2),
            data("/../../../", "..", 3),
            data("aabbababaabb", "ab", 4),
    };

    @Theory
    public void shouldCountWith(Data data) throws Exception {
        assumeThat(data.token(), not(isEmptyOrNullString()));

        // given
        String token = data.token();
        String target = data.target();

        // when
        //noinspection ConstantConditions
        int result = Strings.countToken(target, token);

        // then
        assertThat(result, is(data.count()));
    }

    @Theory
    public void shouldThrowWith(Data data) throws Exception {
        assumeThat(data.token(), isEmptyOrNullString());

        // given
        String token = data.token();
        String target = data.target();

        // expect
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(startsWith("Expected non-empty token"));

        //noinspection ConstantConditions
        Strings.countToken(target, token);
    }

    @AutoValue
    public static abstract class Data {

        public static Data data(@Nullable String target, @Nullable String token, int count) {
            return new AutoValue_StringsCountTest_Data(target, token, count);
        }

        public abstract @Nullable String target();
        public abstract @Nullable String token();
        public abstract int count();
    }
}