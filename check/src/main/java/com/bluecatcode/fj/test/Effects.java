package com.bluecatcode.fj.test;

import fj.P2;
import fj.function.Effect1;
import fj.test.CheckResult;

import static fj.test.CheckResult.summary;
import static java.lang.System.out;
import static org.junit.Assert.assertFalse;

public class Effects {

    private Effects() {
        throw new UnsupportedOperationException();
    }

    public static final Effect1<P2<String, CheckResult>> doSummary = new Effect1<P2<String, CheckResult>>() {
        public void f(final P2<String, CheckResult> result) {
            summary.print(result._2());
            out.println(" (" + result._1() + ')');
        }
    };

    public static final Effect1<P2<String, CheckResult>> doAssertSummary = new Effect1<P2<String, CheckResult>>() {
        public void f(final P2<String, CheckResult> result) {
            String summary = CheckResult.summary.showS(result._2());
            out.printf("%s (%s)\n", summary, result._1());
            assertFalse(summary, result._2().isFalsified());
        }
    };

}
