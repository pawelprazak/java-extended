package com.bluecatcode.common.base;

import org.junit.Test;

import static com.bluecatcode.common.base.RichEnum.Constants.enumConstants;

public class RichEnumTest {

    public static enum TestEnum implements RichEnum<TestEnum> {
        TEST_ENUM;

        public static final Constants<TestEnum> constants = enumConstants(TestEnum.class);

        @Override
        public TestEnum self() {
            return this;
        }
    }

    @Test
    public void shouldTrait() throws Exception {
        // given


        // when
        TestEnum.constants.contains("TEST_ENUM");
        TestEnum.TEST_ENUM.nameEqualsIgnoreCaseAndUnderscore("testenum");

        // then
    }
}
