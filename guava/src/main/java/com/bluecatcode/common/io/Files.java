package com.bluecatcode.common.io;

import com.bluecatcode.common.base.Consumer;
import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.io.CharSource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.nullToEmpty;
import static java.lang.String.format;

/**
 * @see com.google.common.io.Files
 */
@Beta
public class Files {

    private static final Logger log = Logger.getLogger(Files.class.getName());

    public static Properties getFileAsProperties(String path) {
        checkFileExists(path);
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(path)) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return properties;
    }

    public static void checkFilesExist(String... paths) {
        for (String path : paths) {
            checkFileExists(path);
        }
    }

    public static void checkFileExists(String path) {
        File file = new File(nullToEmpty(path));
        checkFileExists(file);
    }

    public static void checkFileExists(File file) {
        checkArgument(file != null, "File is null");

        //noinspection ConstantConditions
        File parentFile = file.getParentFile();
        String fileMessage = format("%s; exists: %s, can read: %s", file, file.exists(), file.canRead());
        String parentFileMessage = (parentFile == null) ? "" :
                format(", dir exists: %s, dir can read: %s", parentFile.exists(), parentFile.canRead());

        log.log(Level.FINE, fileMessage + parentFileMessage);

        checkArgument(file.exists(), "File doesn't exist: '%s'", file);
        checkArgument(file.isFile(), "File isn't normal file: '%s'", file);
        checkArgument(file.canRead(), "File can't be read: '%s'", file);
        checkArgument(parentFile != null, "File parent is null");
        //noinspection ConstantConditions
        checkArgument(parentFile.exists(), "File parent doesn't exist: '%s'", parentFile);
        checkArgument(parentFile.canRead(), "File parent can't be read: '%s'", parentFile);
    }

    public static void consumeLines(File file, Consumer<String> consumer) {
        consumeLines(file, Charsets.UTF_8, consumer);
    }

    public static void consumeLines(File file, Charset charset, Consumer<String> consumer) {
        try {
            CharSource charSource = com.google.common.io.Files.asCharSource(file, charset);
            try (BufferedReader bufferedReader = charSource.openBufferedStream()) {
                for (String line; (line = bufferedReader.readLine()) != null; ) {
                    try {
                        consumer.accept(line);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Files() {
        throw new UnsupportedOperationException();
    }

}
