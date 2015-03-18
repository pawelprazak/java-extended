package com.bluecatcode.common.io;

import com.google.common.base.Objects;
import com.google.common.io.LineProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Properties;

import static com.bluecatcode.common.base.Postconditions.assertNotNull;
import static com.bluecatcode.common.base.Preconditions.checkNotEmpty;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.Resources.readLines;

/**
 * Provides utility methods for working with resources in the classpath.
 *
 * @see com.google.common.io.Resources
 */
public final class Resources {

    /**
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     */
    public static String getResourceAsString(ClassLoader loader, String resourceName) {
        checkNotNull(resourceName);
        checkArgument(!resourceName.isEmpty());

        URL url = loader.getResource(resourceName);
        return toString(assertNotNull(url), UTF_8);
    }

    /**
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     * @throws IllegalArgumentException if the resource is not found
     * @throws IllegalStateException if an I/O error occurs
     */
    public static String getResourceAsString(Class<?> contextClass, String resourceName) {
        checkNotNull(contextClass);
        checkNotEmpty(resourceName);

        URL url = getResource(contextClass, resourceName);
        return toString(url, UTF_8);
    }

    /**
     * @see com.google.common.io.LineProcessor
     * @see com.google.common.io.Resources#getResource(String)
     * @see com.google.common.io.Resources#readLines(java.net.URL, java.nio.charset.Charset, LineProcessor)
     */
    public static <T> T getResourceWith(Class<?> contextClass, String resourceName, LineProcessor<T> lineProcessor) {
        checkNotEmpty(resourceName);
        checkNotNull(lineProcessor);

        URL url = getResource(contextClass, resourceName);
        try {
            return readLines(url, UTF_8, lineProcessor);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     * @throws IllegalStateException if an I/O error occurs
     */
    public static String toString(URL url, Charset charset) {
        try {
            return com.google.common.io.Resources.toString(checkNotNull(url), checkNotNull(charset));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @see com.google.common.io.Resources#getResource(String)
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String getResourceAsFilePath(ClassLoader loader, String resourceName) {
        try {
            return URLDecoder.decode(getResource(loader, resourceName).getFile(), UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @see com.google.common.io.Resources#getResource(String)
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String getResourceAsFilePath(Class<?> class_, String resourceName) {
        try {
            return URLDecoder.decode(getResource(class_, resourceName).getFile(), UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Properties getResourceAsProperties(Class<?> class_, String filename) {
        try (InputStream stream = getResourceAsStream(class_, filename)) {
            Properties properties = new Properties();
            properties.load(stream);
            return assertNotNull(properties, "Can't find %s on classpath for %s", filename, class_);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static InputStream getResourceAsStream(Class<?> class_, String resourceName) {
        checkNotNull(resourceName);
        checkArgument(!resourceName.isEmpty());

        URL url = getResource(class_, resourceName);
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static URL getResource(Class<?> contextClass, String resourceName) {
        return com.google.common.io.Resources.getResource(contextClass, resourceName);
    }

    public static URL getResource(ClassLoader loader, String resourceName) {
        URL url = loader.getResource(resourceName);
        checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
    }

    /**
     * Returns current thread context class loader or {@link Resources} class loader
     */
    public static ClassLoader getContextClassLoader() {
        return getContextClassLoader(Resources.class);
    }

    /**
     * Returns current thread context class loader or context class's class loader
     */
    public static ClassLoader getContextClassLoader(Class<?> contextClass) {
        return Objects.firstNonNull(
                Thread.currentThread().getContextClassLoader(),
                contextClass.getClassLoader()
        );
    }

    private Resources() {
        throw new UnsupportedOperationException();
    }
}
