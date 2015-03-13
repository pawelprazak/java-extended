package com.bluecatcode.common.io;

import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.SystemUtils;
import org.junit.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.bluecatcode.common.io.FilesTest.Data.data;
import static java.nio.file.Files.setPosixFilePermissions;
import static java.nio.file.Paths.get;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Ignore
@RunWith(Theories.class)
public class FilesTest {

    @ClassRule
    public static TemporaryFolder t = new TemporaryFolder();

    public static final Set<PosixFilePermission> O_664 = fromString("rw-rw-r--");
    public static final Set<PosixFilePermission> O_220 = fromString("-w-rw----");
    public static final Set<PosixFilePermission> O_375 = fromString("-wxrwxr-x");
    public static final Set<PosixFilePermission> O_675 = fromString("rw-rwxr-x");

    @DataPoints
    public static final Data[] samples = new Data[]{
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return null;
                }
            }, O_664,
                    "File doesn't exist: ''"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return new File("");
                }
            }, O_664,
                    "File doesn't exist: ''"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return t.newFile("ordinary_directory");
                }
            }, O_664,
                    "File isn't normal file: '/tmp/ordinary_directory'"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return new File("nonexistent.file");
                }
            }, O_664,
                    "File doesn't exist: '/tmp/nonexistent.file'"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return new File("unreadable.file");
                }
            }, O_220,
                    "File can't be read: '/tmp/unreadable.file'"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    File d = t.newFolder("forbidden_directory");
                    setPosixFilePermissions(get(d.getPath()), O_675);
                    return t.newFile("ordinary.file");
                }
            }, O_664, "File doesn't exist: '/tmp/forbidden_directory/ordinary.file'"),
            data(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    File d = t.newFolder("unreadable_directory");
                    setPosixFilePermissions(get(d.getPath()), O_375);
                    return t.newFile("ordinary.file");
                }
            }, O_664, "File parent can't be read: '/tmp/unreadable_directory'"),
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TestRule ignoreIfNotUnix = new TestRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    Assume.assumeTrue("This test requires Unix like operating system.", SystemUtils.IS_OS_UNIX);
                }
            };
        }
    };

    @Theory
    public void shouldFailWithFile(Data sample) throws Exception {
        // given
        File file = sample.file().call();
        Set<PosixFilePermission> posixFilePermissions = sample.permissions();
        setPosixFilePermissions(get(file.getPath()), posixFilePermissions);

        // expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(sample.expectedExceptionMessage());

        // when
        Files.checkFileExists(file);
    }

    @Theory
    public void shouldFailWithPath(Data sample) throws Exception {
        // given
        File file = sample.file().call();
        Set<PosixFilePermission> posixFilePermissions = sample.permissions();
        setPosixFilePermissions(get(file.getPath()), posixFilePermissions);

        // expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(sample.expectedExceptionMessage());

        // when
        System.err.println("# " + file.getPath());
        Files.checkFileExists(file.getPath());
    }

    @Test
    public void shouldFail() throws IOException {

        // expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("File is null");

        // when
        //noinspection ConstantConditions
        Files.checkFileExists((String) null);
    }

    @Test
    public void shouldPass() throws IOException {
        // given
        File file = t.newFile("/tmp/ordinary.file");

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
        t.newFile(path1);
        t.newFile(path2);
        t.newFile(path3);

        // when
        Files.checkFilesExist(path1, path2, path3);
    }

    @AutoValue
    public static abstract class Data {

        public static Data data(Callable<File> file,
                                Set<PosixFilePermission> permissions,
                                String expectedExceptionMessage) {
            try {
                return new AutoValue_FilesTest_Data(file, permissions, expectedExceptionMessage);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        public abstract Callable<File> file();
        public abstract Set<PosixFilePermission> permissions();
        public abstract String expectedExceptionMessage();
    }
}