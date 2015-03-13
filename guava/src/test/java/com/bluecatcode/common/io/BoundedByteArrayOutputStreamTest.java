package com.bluecatcode.common.io;

import com.google.common.base.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundedByteArrayOutputStreamTest {

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
}