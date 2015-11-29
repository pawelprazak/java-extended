package com.bluecatcode.time;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MillisecondsTest {

    @Test
    public void testDays() throws Exception {
        assertThat(Milliseconds.days(0L), is(0L));
    }

    @Test
    public void testHours() throws Exception {
        assertThat(Milliseconds.hours(0L), is(0L));
    }

    @Test
    public void testMinutes() throws Exception {
        assertThat(Milliseconds.minutes(0L), is(0L));
    }

    @Test
    public void testSeconds() throws Exception {
        assertThat(Milliseconds.seconds(0L), is(0L));
    }

    @Test
    public void testMinutesInts() throws Exception {
        assertThat(Milliseconds.minutes(0), is(0));
    }

    @Test
    public void testSecondsInts() throws Exception {
        assertThat(Milliseconds.seconds(0), is(0));
    }
}