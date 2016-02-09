package com.bluecatcode.junit.rules;

import com.google.common.jimfs.Configuration;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemporaryFileSystemRuleTest {

    @ClassRule
    public static TemporaryFileSystemRule vfs = new TemporaryFileSystemRule(Configuration.windows());

    @Test
    public void shouldCreateDirAndFile() throws Exception {
        Path foo = vfs.getPath("foo");
        Files.createDirectory(foo);
        Path filePath = foo.resolve("bar.file");

        System.out.println(System.getProperty(TemporaryFileSystemRule.SPI_DEFAULT_FILE_SYSTEM_PROVIDER));
        File tempFile = File.createTempFile("foo", "bar");
        System.out.println("tempFile = " + tempFile);

        System.out.println("vfs = " + vfs.getRootDirectories());
//        File file = new File(filePath.toUri());
    }
}