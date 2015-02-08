package com.bluecatcode.common.time;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ISO8601Test {

    @Test
    public void shouldRoundTrip() throws Exception {
        // given
        String inputDate = "2014-11-29T00:00:00+02:00";

        // when
        Date date     = ISO8601.isoToDate(inputDate);
        String actual = ISO8601.dateToIso(date);

        // then
        assertThat(actual, is("2014-11-28T22:00:00Z"));
    }

    @Test
    public void shouldRoundTripWithJoda() throws Exception {
        // given
        String inputDateTime = "2014-11-29T00:00:00+02:00";

        // when
        DateTime dateTime = ISO8601.isoToDateTime(inputDateTime);
        String actual = ISO8601.dateTimeToIso(dateTime);

        // then
        assertThat(actual, is("2014-11-28T22:00:00Z"));
    }
}