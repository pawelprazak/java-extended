package com.bluecatcode.common.base;

import com.bluecatcode.common.contract.errors.RequireViolation;
import com.bluecatcode.common.functions.Block;
import com.bluecatcode.common.functions.CheckedBlock;
import com.bluecatcode.common.functions.CheckedFunction;
import com.bluecatcode.common.functions.Function;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests that need static typing, e.g.:
 * see https://issues.apache.org/jira/browse/GROOVY-2882
 * or https://github.com/spockframework/spock/issues/589
 *
 * @see EithersSpec
 */
public class EithersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowOnEitherCallableNullArgument() throws Exception {
        // given
        Callable callable = null;

        // expect
        exception.expect(RequireViolation.class);

        // when
        //noinspection ConstantConditions
        Eithers.either(callable);
    }

    @Test
    public void shouldCreateEitherCallableErrorResult() throws Exception {
        // given
        Callable callable = () -> {
            throw new RuntimeException();
        };

        // when
        Either either = Eithers.either(callable);

        // then
        assertThat(either.isError(), is(true));
    }

    @Test
    public void shouldThrowOnEitherBlockNullArgument() throws Exception {
        // given
        Block block = null;

        // expect
        exception.expect(RequireViolation.class);

        // when
        //noinspection ConstantConditions
        Eithers.either(block);
    }

    @Test
    public void shouldCreateEitherBlockErrorResult() throws Exception {
        // given
        Block block = () -> {
            throw new RuntimeException();
        };

        // when
        Either either = Eithers.either(block);

        // then
        assertThat(either.isError(), is(true));
    }


    @Test
    public void shouldThrowOnEitherCheckedBlockNullArgument() throws Exception {
        // given
        CheckedBlock block = null;

        // expect
        exception.expect(RequireViolation.class);

        // when
        //noinspection ConstantConditions
        Eithers.either(block);
    }

    @Test
    public void shouldCreateEitherCheckedBlockErrorResult() throws Exception {
        // given
        CheckedBlock block = () -> {
            throw new IOException();
        };

        // when
        Either either = Eithers.either(block);

        // then
        assertThat(either.isError(), is(true));
    }

    @Test
    public void shouldThrowOnEitherFunctionNullArgument() throws Exception {
        // given
        Function function = null;

        // expect
        exception.expect(RequireViolation.class);

        // when
        //noinspection ConstantConditions
        Eithers.either(function);
    }

    @Test
    public void shouldCreateEitherFunctionErrorResult() throws Exception {
        // given
        Function<String, Either<String, Exception>> function = input -> {
            throw new RuntimeException();
        };

        // when
        Either either = Eithers.either(function).apply("Anything");

        // then
        assertThat(either, is(notNullValue()));
        //noinspection ConstantConditions
        assertThat(either.isError(), is(true));
        assertThat(either.isValue(), is(false));
    }

    @Test
    public void shouldThrowOnEitherCheckedFunctionNullArgument() throws Exception {
        // given
        CheckedFunction function = null;

        // expect
        exception.expect(RequireViolation.class);

        // when
        //noinspection ConstantConditions
        Eithers.either(function);
    }

    @Test
    public void shouldCreateEitherCheckedFunctionErrorResult() throws Exception {
        // given
        CheckedFunction<String, Either<String, Exception>, IOException> function = input -> {
            throw new IOException();
        };

        // when
        Either either = Eithers.either(function).apply("Anything");

        // then
        assertThat(either, is(notNullValue()));
        //noinspection ConstantConditions
        assertThat(either.isError(), is(true));
        assertThat(either.isValue(), is(false));
    }

    @Test
    public void verifyEqualsAndHashCode() throws Exception {
        EqualsVerifier.forClass(Left.class).suppress(Warning.NULL_FIELDS).verify();
        EqualsVerifier.forClass(Right.class).suppress(Warning.NULL_FIELDS).verify();
    }
}