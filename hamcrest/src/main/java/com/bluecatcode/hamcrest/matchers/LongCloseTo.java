package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Matcher;

public class LongCloseTo extends ComparableCloseTo<Long> {

    public LongCloseTo(Long value, Long error) {
        super(error, value);
    }

    @Override
    protected Long zero() {
        return 0L;
    }

    @Override
    protected Long actualDelta(Long item) {
        return (Math.abs((item - value)) - delta);
    }

    /**
     * Creates a matcher of {@link Long}s that matches when an examined Long is equal
     * to the specified <code>operand</code>, within a range of +/- <code>error</code>. The comparison for equality
     * is done by BigDecimals {@link Long#compareTo(Long)} method.
     * <p>
     * For example:
     * <pre>assertThat(103L, is(closeTo(100, 0.03)))</pre>
     * </p>
     *
     * @param operand the expected value of matching Long
     * @param error   the delta (+/-) within which matches will be allowed
     * @return the matcher
     */
    public static Matcher<Long> closeTo(Long operand, Long error) {
        return new LongCloseTo(operand, error);
    }

}
