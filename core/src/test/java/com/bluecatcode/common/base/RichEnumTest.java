package com.bluecatcode.common.base;

import org.junit.Test;

import javax.annotation.Nullable;

import static com.bluecatcode.common.base.AbstractRichEnum.Constants;
import static com.bluecatcode.common.base.AbstractRichEnum.Constants.enumConstants;

public class RichEnumTest {

    public static enum TestEnum implements RichEnum<TestEnum> {
        TEST_ENUM;

        private final AbstractRichEnum<TestEnum> rich;

        TestEnum() {
            this.rich = new AbstractRichEnum<TestEnum>() {
                @Override
                public TestEnum self() {
                    return TestEnum.this;
                }
            };
        }

        public static final Constants<TestEnum> constants = enumConstants(TestEnum.class);

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
    public void shouldTrait() throws Exception {
        // given


        // when
        TestEnum.constants.contains("TEST_ENUM");
        TestEnum.TEST_ENUM.nameEqualsIgnoreCaseAndUnderscore("testenum");

        // then
    }
}
