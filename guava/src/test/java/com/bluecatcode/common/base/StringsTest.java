package com.bluecatcode.common.base;

import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StringsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldMapAsString() throws Exception {
        // given
        ImmutableMap<String, Integer> map = of("a", 1, "b", 2);

        // when
        String result = Strings.asString(map);

        // then
        assertThat(result, not(isEmptyOrNullString()));
        assertThat(result, is("a: 1, b: 2"));
    }

    @Test
    public void shouldSplitStringAsStringList() throws Exception {
        // given
        String input = "a\nb\r\n\\c\t\r\n";

        // when
        List<String> result = Strings.asStringList(input);

        // then
        assertThat(result, hasSize(3));
        assertThat(result, hasItems(
                "a", "b", "\\c\t"
        ));
    }

    @Test
    public void shouldThrowOnNullString() throws Exception {
        // given
        String input = null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        //noinspection ConstantConditions
        Strings.asStringList(input);
    }
}