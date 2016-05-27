package com.bluecatcode.common.io;

import com.bluecatcode.common.base.functions.Consumer;
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
 * @since 1.0
 */
@Beta
public class Files {

    private static final Logger log = Logger.getLogger(Files.class.getName());

    private Files() {
        throw new UnsupportedOperationException();
    }

    /**
     * Loads properties from file path
     * @param path the path to properties file
     * @return the loaded properties
     */
    public static Properties getFileAsProperties(String path) {
        File file = new File(nullToEmpty(path));
        return getFileAsProperties(file);
    }

    /**
     * Loads properties from file
     * @param file the properties file
     * @return the loaded properties
     *
     * @since 1.0.4
     */
    public static Properties getFileAsProperties(File file) {
        checkFileExists(file);
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return properties;
    }


    public static void checkFilesExist(File... files) {
        for (File file : files) {
            checkFileExists(file);
        }
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
        checkArgument(file != null, "Expected non-null file");

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

    public static void write(CharSequence from, File to) throws IOException {
        write(from, to, Charsets.UTF_8);
    }

    public static void write(CharSequence from, File to, Charset charset) throws IOException {
        com.google.common.io.Files.write(from, to, charset);
    }

    public static void append(CharSequence from, File to) throws IOException {
        append(from, to, Charsets.UTF_8);
    }

    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        com.google.common.io.Files.append(from, to, charset);
    }
}