package com.bluecatcode.fj.test;

import org.junit.Test;

import static com.bluecatcode.fj.test.Effects.doAssertSummary;
import static fj.test.reflect.Check.check;

public class FunctionalJavaCheck<T> {

    @Test
    public void shouldPassWithProperties() throws Exception {
        check(this.getClass()).foreach(doAssertSummary);
    }

}
