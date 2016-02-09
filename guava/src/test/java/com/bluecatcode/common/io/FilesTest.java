package com.bluecatcode.common.io;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static com.bluecatcode.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;

public class FilesTest {

    @ClassRule
    public static TemporaryFolder tmp = new TemporaryFolder();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldFail() throws IOException {
        // given
        String path = null;

        // expect
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(startsWith("File doesn't exist"));

        // when
        //noinspection ConstantConditions
        Files.checkFileExists(path);
    }

    @Test
    public void shouldPass() throws IOException {
        // given
        File file = tmp.newFile("test-ordinary.file");

        // when
        Files.checkFileExists(file);
        Files.checkFileExists(file.getPath());
    }

    @Test
    public void shouldGetFileAsProperties() throws IOException {
        // given
        File file = tmp.newFile("example_config.properties");

        try (OutputStream output = new FileOutputStream(file)) {
            Properties prop = new Properties();
            prop.setProperty("example.key", "example_value");
            prop.store(output, "test properties");
        }

        // when
        Properties returnedProperties = Files.getFileAsProperties(file.getAbsolutePath());

        // then
        assertThat(returnedProperties.getProperty("example.key"), is("example_value"));
    }

    @Test
    public void shouldPassWithMultipleFiles() throws IOException {
        // given
        String path1 = "test-more.file";
        String path2 = "test-another.file";
        String path3 = "test-further.file";
        File file1 = tmp.newFile(path1);
        File file2 = tmp.newFile(path2);
        File file3 = tmp.newFile(path3);

        // when
        Files.checkFilesExist(file1, file2, file3);
        Files.checkFilesExist(file1.getAbsolutePath(), file2.getAbsolutePath(), file3.getAbsolutePath());
    }

    @Test
    public void shouldConsumeLines() throws Exception {
        // given
        String expectedValue = "Hello world\n";
        File file = tmp.newFile("test-hello.txt");
        Files.write(expectedValue, file);
        Files.append(expectedValue, file);

        StringBuilder builder = new StringBuilder();

        // when
        Files.consumeLines(file, (s) -> builder.append(s).append('\n'));

        // then
        assertThat(builder, hasSize(expectedValue.length() * 2));
    }
}