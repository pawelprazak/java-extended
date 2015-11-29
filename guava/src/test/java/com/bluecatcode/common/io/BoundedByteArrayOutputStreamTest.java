package com.bluecatcode.common.io;

import com.google.auto.value.AutoValue;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static com.bluecatcode.common.io.BoundedByteArrayOutputStreamTest.Data.data;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

@RunWith(Theories.class)
public class BoundedByteArrayOutputStreamTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @DataPoints
    public static final Data[] samples = new Data[]{
            data(new byte[60], 0, 60, IllegalArgumentException.class, startsWith("Reached the buffer limit")),
            data(new byte[30], 31, 1, IndexOutOfBoundsException.class, startsWith("Invalid offset")),
            data(new byte[30], -1, 30, IndexOutOfBoundsException.class, startsWith("Invalid offset")),
            data(new byte[30], 0, 31, IndexOutOfBoundsException.class, startsWith("Invalid length")),
    };

    @Theory
    public void testWriteBoundWith(Data data) throws Exception {
        // given
        BoundedByteArrayOutputStream stream = new BoundedByteArrayOutputStream(32);

        // expect
        exception.expect(data.expectedException());
        exception.expectMessage(data.messageMatcher());

        // when
        stream.write(data.payload(), data.offset(), data.length());
    }

    @Test
    public void testWriteBound() throws Exception {
        // given
        BoundedByteArrayOutputStream stream = new BoundedByteArrayOutputStream(1);

        // expect
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(startsWith("Reached the limit of the buffer"));

        // when
        stream.write(1);
        stream.write(1);
    }

    @Test
    public void testWriteEmpty() throws Exception {
        // given
        BoundedByteArrayOutputStream stream = new BoundedByteArrayOutputStream(32);

        // when
        stream.write(new byte[0]);

        // then
        assertThat(stream.size(), is(0));
    }

    @Test
    public void testWriting() throws IOException {
        // given
        BoundedByteArrayOutputStream stream = new BoundedByteArrayOutputStream(32);

        // compare this implementation
        for (int i = 0; i < 10; i++) {

            // make sure reset clears up the buffer
            stream.reset();

            // compare with standard java.io backed implementation
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            runComparison(stream, dos, bos);
        }
    }

    private void runComparison(BoundedByteArrayOutputStream aos, DataOutputStream dos,
                               ByteArrayOutputStream bos) throws IOException {
        Random r = new Random();

        // byte
        int b = r.nextInt(128);
        aos.write(b);
        dos.write(b);

        // byte[]
        byte[] bytes = new byte[10];
        r.nextBytes(bytes);
        aos.write(bytes, 0, 10);
        dos.write(bytes, 0, 10);

        // strings
        String str = RandomStringUtils.random(10);
        byte[] strBytes = str.getBytes(Charsets.UTF_8);
        aos.write(strBytes, 0, 10);
        dos.write(strBytes, 0, 10);

        byte[] expected = bos.toByteArray();
        assertEquals(expected.length, aos.size());

        byte[] actual = new byte[aos.size()];
        System.arraycopy(aos.toByteArray(), 0, actual, 0, actual.length);
        // serialized bytes should be the same
        assertTrue(Arrays.equals(expected, actual));
    }

    @AutoValue
    public static abstract class Data {

        public static Data data(byte[] payload, int offset, int length, Class<? extends Throwable> expectedException, Matcher<String> messageMatcher) {
            return new AutoValue_BoundedByteArrayOutputStreamTest_Data(payload, offset, length, expectedException, messageMatcher);
        }

        public abstract byte[] payload();
        public abstract int offset();
        public abstract int length();

        public abstract Class<? extends Throwable> expectedException();
        public abstract Matcher<String> messageMatcher();
    }
}