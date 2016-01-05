package com.bluecatcode.common.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import javax.annotation.Nullable;

import static com.bluecatcode.common.concurrent.Futures.futureWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FuturesTest {

    @Test
    public void shouldSubmitTask() throws Exception {
        // given
        /* Use @Deprecated method for backward compatibility */
        ListeningExecutorService executor = MoreExecutors.sameThreadExecutor();

        Function<Object, String> task = Object::toString;

        final int[] counts = new int[]{0, 0};
        FutureCallback<String> callback = new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String result) {
                counts[0]++;
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onFailure(Throwable t) {
                counts[1]++;
            }
        };

        // when
        futureWith(executor, task, callback).apply(null);
        futureWith(executor, task, callback).apply(1L);

        // then
        assertThat(counts[0], is(1));
        assertThat(counts[1], is(1));
    }
}