package com.bluecatcode.junit.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nullable;

import static com.bluecatcode.junit.rules.RepeatRule.RepeatStatement.createFromAnnotationWith;

public class RepeatRule implements TestRule {

    static class RepeatStatement extends Statement {

        private final int times;
        private final Statement statement;

        private RepeatStatement(int times, Statement statement) {
            this.times = times;
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            for (int i = 0; i < times; i++) {
                statement.evaluate();
            }
        }

        @Nullable
        public static Statement createFromAnnotationWith(@Nullable Repeat repeat, Statement statement) {
            if (statement == null) {
                throw new IllegalArgumentException("Expected non-null statement");
            }

            if (repeat == null) {
                return null;
            }

            int times = repeat.times();
            return new RepeatStatement(times, statement);
        }
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        Repeat repeat = description.getAnnotation(Repeat.class);
        Statement statement1 = createFromAnnotationWith(repeat, statement);
        return statement1 == null ? statement : statement1;
    }

}
