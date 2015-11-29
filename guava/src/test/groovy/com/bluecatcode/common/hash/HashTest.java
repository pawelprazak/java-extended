package com.bluecatcode.common.hash;

import org.junit.Test;

import static com.bluecatcode.common.hash.Hash.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HashTest {

    @Test
    public void testMd5AsString() throws Exception {
        assertThat(md5AsString("łół"), is("c1feec7f85528062cce1e44c5feee713"));
    }

    @Test
    public void testSha1AsString() throws Exception {
        assertThat(sha1AsString("łół"), is("7a2cb7913f07d4f8909bb884673efe3a7112c16b"));
    }

    @Test
    public void testSha256AsString() throws Exception {
        assertThat(sha256AsString("łół"), is("f85feb330139c08fc75da6b01c6d352c50ffd6c7a6deef1c5ec7d68a44054e3a"));
    }

    @Test
    public void testSha512AsString() throws Exception {
        assertThat(sha512AsString("łół"), is("8d73ad158d10ce41ed0ea99e0af77c48b43f1688f27f8a264cbc83d10a5b92969e5c4d1a2db18b6bd0231b502c6d68b3bcc4b33d4be2c08e5ed4b99d73966d69"));
    }
}