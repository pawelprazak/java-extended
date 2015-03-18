package com.bluecatcode.common.base;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;

import static com.bluecatcode.common.base.StringsLimitTest.Data.data;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class StringsLimitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @DataPoints
    public static final Data[] samples = new Data[]{
            data(null, 0, null, IllegalArgumentException.class),
            data("", -1, null, IllegalArgumentException.class),
            data("", 0, ""),
            data("ccc", 0, ""),
            data("abcd", 4, "abcd"),
            data("abcdefgh", 7, "abcd..."),
            data("aaaaa", 1, "a"),
            data("aaaaa", 2, "aa"),
            data("aaaaa", 3, "aaa"),
            data("aaaaa", 4, "a..."),
    };

    @Theory
    public void shouldLimit(Data data) throws Exception {
        // given
        String input = data.input();
        int maxLength = data.max();
        String expected = data.output();

        if (data.exceptionType().isPresent()) {
            // expect
            exception.expect(data.exceptionType().get());
        }

        // when
        String result = Strings.limitCharacters(input, maxLength);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result, is(expected));
        assertThat(result.length(), is(maxLength));
    }

    @AutoValue
    public static abstract class Data {

        public static Data data(String input, int max, String output) {
            return new AutoValue_StringsLimitTest_Data(input, max, output, Optional.<Class<? extends Throwable>>absent());
        }

        public static Data data(String input, int max, String output, Class<? extends Throwable> exceptionType) {
            return new AutoValue_StringsLimitTest_Data(input, max, output, Optional.<Class<? extends Throwable>>of(exceptionType));
        }

        @Nullable
        public abstract String input();
        public abstract int max();
        @Nullable
        public abstract String output();
        public abstract Optional<Class<? extends Throwable>> exceptionType();
    }
}