package com.bluecatcode.common.base;

import com.google.common.base.Function;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests that need static typing, see https://issues.apache.org/jira/browse/GROOVY-2882
 *
 * @see EitherSpec
 */
public class EitherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowOnEitherCallableNullArgument() throws Exception {
        // given
        Callable callable = null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        //noinspection ConstantConditions
        Either.either(callable);
    }

    @Test
    public void shouldThrowOnEitherCallableNullResult() throws Exception {
        // given
        Callable callable = () -> null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        Either.either(callable);
    }

    @Test
    public void shouldCreateEitherCallableValueResult() throws Exception {
        // given
        Callable callable = () -> 1;

        // when
        Either either = Either.either(callable);

        // then
        assertThat(either.isValue(), is(true));
    }

    @Test
    public void shouldCreateEitherCallableErrorResult() throws Exception {
        // given
        Callable callable = () -> {
            throw new RuntimeException();
        };

        // when
        Either either = Either.either(callable);

        // then
        assertThat(either.isError(), is(true));
    }

    @Test
    public void shouldThrowOnEitherBlockNullArgument() throws Exception {
        // given
        Block block = null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        //noinspection ConstantConditions
        Either.either(block);
    }

    @Test
    public void shouldThrowOnEitherBlockNullResult() throws Exception {
        // given
        Block block = () -> null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        Either.either(block);
    }

    @Test
    public void shouldCreateEitherBlockValueResult() throws Exception {
        // given
        Block block = () -> 1;

        // when
        Either either = Either.either(block);

        // then
        assertThat(either.isValue(), is(true));
    }

    @Test
    public void shouldCreateEitherBlockErrorResult() throws Exception {
        // given
        Block block = () -> {
            throw new RuntimeException();
        };

        // when
        Either either = Either.either(block);

        // then
        assertThat(either.isError(), is(true));
    }

    @Test
    public void shouldThrowOnEitherFunctionNullArgument() throws Exception {
        // given
        Function function = null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        //noinspection ConstantConditions
        Either.either(function);
    }

    @Test
    public void shouldThrowOnEitherFunctionNullResult() throws Exception {
        // given
        Function<String, Integer> function = input -> null;

        // expect
        exception.expect(IllegalArgumentException.class);

        // when
        //noinspection ConstantConditions
        Either.either(function).apply("Anything");
    }

    @Test
    public void shouldCreateEitherFunctionValueResult() throws Exception {
        // given
        Function<String, Integer> function = input -> 0;

        // when
        Either either = Either.either(function).apply("Anything");

        // then
        assertThat(either, is(notNullValue()));
        //noinspection ConstantConditions
        assertThat(either.isValue(), is(true));
        assertThat(either.isError(), is(false));
    }

    @Test
    public void shouldCreateEitherFunctionErrorResult() throws Exception {
        // given
        Function<String, Either<String, Exception>> function = input -> {
            throw new RuntimeException();
        };

        // when
        Either either = Either.either(function).apply("Anything");

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