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

import java.util.concurrent.Callable;

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
    public void verifyEqualsAndHashCode() throws Exception {
        EqualsVerifier.forClass(Left.class).suppress(Warning.NULL_FIELDS).verify();
        EqualsVerifier.forClass(Right.class).suppress(Warning.NULL_FIELDS).verify();
    }
}