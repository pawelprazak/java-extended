package com.bluecatcode.common.base;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Nullable;

import static com.bluecatcode.common.base.RichEnumConstants.richConstants;
import static com.bluecatcode.common.base.RichEnumInstance.richEnum;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RichEnumTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static Matcher<String> isNotEmptyOrNullString = not(isEmptyOrNullString());

    public enum TestEnum implements RichEnum<TestEnum> {
        TEST_ENUM;

        public static final RichEnumConstants<TestEnum> constants = richConstants(TestEnum.class);

        private final RichEnumInstance<TestEnum> rich = richEnum(this);

        @Override
        public boolean nameEquals(@Nullable String that) {
            return rich.nameEquals(that);
        }

        @Override
        public boolean nameEqualsIgnoreCase(@Nullable String that) {
            return rich.nameEqualsIgnoreCase(that);
        }

        @Override
        public boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that) {
            return rich.nameEqualsIgnoreCaseAndUnderscore(that);
        }
    }

    @Test
    public void testAllContains() throws Exception {
        assertThat(TestEnum.constants.contains(TestEnum.TEST_ENUM), is(true));
        assertThat(TestEnum.constants.contains("TEST_ENUM"), is(true));
        assertThat(TestEnum.constants.containsIgnoreCase("test_enum"), is(true));
        assertThat(TestEnum.constants.containsIgnoreCaseAndUnderscore("tEsT__eNuM"), is(true));

        assertThat(TestEnum.constants.contains((String) null), is(false));
        assertThat(TestEnum.constants.contains((TestEnum) null), is(false));
        assertThat(TestEnum.constants.contains(""), is(false));
        assertThat(TestEnum.constants.contains("NONE"), is(false));
        assertThat(TestEnum.constants.contains("test_enum"), is(false));
        assertThat(TestEnum.constants.containsIgnoreCase("testenum"), is(false));
        assertThat(TestEnum.constants.containsIgnoreCaseAndUnderscore("testenummmmm"), is(false));
    }

    @Test
    public void testValueOf() throws Exception {
        assertThat(TestEnum.constants.valueOf("TEST_ENUM"), is(TestEnum.TEST_ENUM));
        assertThat(TestEnum.constants.valueOfIgnoreCase("tEsT_eNuM"), is(TestEnum.TEST_ENUM));
        assertThat(TestEnum.constants.valueOfIgnoreCaseAndUnderscore("tEsT__eNuM"), is(TestEnum.TEST_ENUM));
    }

    @Test
    public void testNamesAndAsString() throws Exception {
        assertThat(TestEnum.constants.names().toList(), hasSize(TestEnum.values().length));
        assertThat(TestEnum.constants.names().toList(), hasItem(TestEnum.TEST_ENUM.name()));
        assertThat(TestEnum.constants.asString(), containsString(TestEnum.TEST_ENUM.toString()));
        assertThat(TestEnum.constants.asString(), isNotEmptyOrNullString);
        assertThat(TestEnum.constants.toString(), isNotEmptyOrNullString);
    }

    @Test
    public void testEnumConstantsThrowOnNull() throws Exception {
        // expect
        exception.expect(NullPointerException.class);

        // when
        //noinspection ConstantConditions
        RichEnumConstants.richConstants(null);
    }

    @Test
    public void testEnumConstantsCreatesObject() throws Exception {
        // given
        Class<TestEnum> theClass = TestEnum.class;

        // when
        RichEnumConstants<TestEnum> constants = RichEnumConstants.richConstants(theClass);

        // then
        assertThat(constants, is(notNullValue()));
    }
}
