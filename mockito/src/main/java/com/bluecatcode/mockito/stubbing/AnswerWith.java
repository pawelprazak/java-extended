package com.bluecatcode.mockito.stubbing;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nullable;
import java.util.LinkedList;

/**
 * Mockito {@link Answer} with multiple throwables and then return values,
 * e.g. <code>given(...).will(answerWith(..., ...)</code>
 * @param <T> the answer type
 */
public class AnswerWith<T> implements Answer<T> {

    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    private final Iterable<? extends Throwable> throwables;
    private final Iterable<? extends T> values;

    public AnswerWith(Iterable<? extends Throwable> throwables, Iterable<? extends T> returnValues) {
        if (throwables == null) {
            throw new MockitoException("AnswerWith does not accept null as constructor argument.\n" +
                    "Please pass throwables, can be empty");
        }
        if (returnValues == null) {
            throw new MockitoException("AnswerWith does not accept null as constructor argument.\n" +
                    "Please pass return values, can be empty");
        }
        this.throwables = throwables;
        this.values = returnValues;
    }

    public static <T> AnswerWith<T> answerWith(Iterable<? extends Throwable> throwables, Iterable<? extends T> returnValues) {
        return new AnswerWith<T>(throwables, returnValues);
    }

    public static <T> AnswerWith<T> answerWith(Iterable<? extends Throwable> throwables) {
        return new AnswerWith<T>(throwables, new LinkedList<>());
    }

    @Nullable
    public T answer(InvocationOnMock invocation) throws Throwable {
        if (throwables.iterator().hasNext()) {
            Throwable throwable = throwables.iterator().next();
            if (new MockUtil().isMock(throwable)) {
                throw throwable;
            }
            Throwable t = throwable.fillInStackTrace();
            filter.filter(t);
            throw t;
        }
        if (values.iterator().hasNext()) {
            return values.iterator().next();
        } else {
            return null;
        }
    }
}