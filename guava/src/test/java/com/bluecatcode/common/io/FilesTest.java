package com.bluecatcode.common.io;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assume;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FilesTest {

    @ClassRule
    public static TemporaryFolder tmp = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TestRule ignoreIfNotUnix = (base, description) -> {
        // This test requires Unix like operating system.
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Assume.assumeThat(SystemUtils.IS_OS_UNIX, is(true));
            }
        };
    };

    @Test
    public void shouldFail() throws IOException {
        // given
        String path = null;

        // expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("File is null");

        // when
        //noinspection ConstantConditions
        Files.checkFileExists(path);
    }

    @Test
    public void shouldPass() throws IOException {
        // given
        File file = tmp.newFile("/tmp/ordinary.file");

        // when
        Files.checkFileExists(file);
        Files.checkFileExists(file.getPath());
    }

    @Test
    public void shouldGetFileAsProperties() throws IOException {
        // given
        String path = "/tmp/example_config.properties";
        try (OutputStream output = new FileOutputStream(path)) {
            Properties prop = new Properties();
            prop.setProperty("example.key", "example_value");
            prop.store(output, "test properties");
        }

        // when
        Properties returnedProperties = Files.getFileAsProperties(path);

        // then
        assertThat(returnedProperties.getProperty("example.key"), is("example_value"));
    }

    @Test
    public void shouldPassWithMultipleFiles() throws IOException {
        // given
        String path1 = "/tmp/more.file";
        String path2 = "/tmp/another.file";
        String path3 = "/tmp/further.file";
        tmp.newFile(path1);
        tmp.newFile(path2);
        tmp.newFile(path3);

        // when
        Files.checkFilesExist(path1, path2, path3);
    }

}