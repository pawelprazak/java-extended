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

import static com.bluecatcode.common.base.Predicates.isNotNull;
import static com.bluecatcode.common.contract.Checks.checkNotEmpty;
import static com.bluecatcode.common.contract.Postconditions.ensure;
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

    private Resources() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param loader       the class loader to use
     * @param resourceName the resource name
     * @return the loaded content
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     */
    public static String getResourceAsString(ClassLoader loader, String resourceName) {
        checkArgument(loader != null, "Expected non-null class loader");
        checkNotEmpty(resourceName);

        URL url = loader.getResource(resourceName);
        ensure(url, isNotNull(), "Expected non-null url for resourceName '%s'", resourceName);
        return toString(url, UTF_8);
    }

    /**
     * @param contextClass the class the resource is relative to
     * @param resourceName the resource name
     * @return the loaded content
     * @throws IllegalArgumentException if the resource is not found
     * @throws IllegalStateException    if an I/O error occurs
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     */
    public static String getResourceAsString(Class<?> contextClass, String resourceName) {
        checkArgument(contextClass != null, "Expected non-null contextClass");
        checkNotEmpty(resourceName);

        URL url = getResource(contextClass, resourceName);
        return toString(url, UTF_8);
    }

    /**
     * @param contextClass  the class the resource is relative to
     * @param resourceName  the resource name
     * @param lineProcessor the line processor to use
     * @param <T>           the type of object to load into
     * @return the loaded object of type <tt>T</tt>
     * @see com.google.common.io.LineProcessor
     * @see com.google.common.io.Resources#getResource(String)
     * @see com.google.common.io.Resources#readLines(java.net.URL, java.nio.charset.Charset, LineProcessor)
     */
    public static <T> T getResourceWith(Class<?> contextClass, String resourceName, LineProcessor<T> lineProcessor) {
        checkArgument(contextClass != null, "Expected non-null contextClass");
        checkNotEmpty(resourceName);
        checkArgument(lineProcessor != null, "Expected non-null lineProcessor");

        URL url = getResource(contextClass, resourceName);
        try {
            return readLines(url, UTF_8, lineProcessor);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param url     the URL to read from
     * @param charset the charset to use
     * @return the content
     * @throws IllegalStateException if an I/O error occurs
     * @see com.google.common.io.Resources#toString(java.net.URL, java.nio.charset.Charset)
     */
    public static String toString(URL url, Charset charset) {
        try {
            return com.google.common.io.Resources.toString(checkNotNull(url), checkNotNull(charset));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param loader       the class loader to use
     * @param resourceName the resource name
     * @return the file path
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
     * @param contextClass the class the resource is relative to
     * @param resourceName the resource name
     * @return the file path
     * @see com.google.common.io.Resources#getResource(String)
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String getResourceAsFilePath(Class<?> contextClass, String resourceName) {
        try {
            return URLDecoder.decode(getResource(contextClass, resourceName).getFile(), UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param contextClass the class the resource is relative to
     * @param resourceName the resource name
     * @return the loaded properties
     */
    public static Properties getResourceAsProperties(Class<?> contextClass, String resourceName) {
        try (InputStream stream = getResourceAsStream(contextClass, resourceName)) {
            Properties properties = new Properties();
            properties.load(stream);
            ensure(properties, isNotNull(), "Can't find %s on classpath for %s", resourceName, contextClass);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param contextClass the class the resource is relative to
     * @param resourceName the resource name
     * @return the input stream
     */
    public static InputStream getResourceAsStream(Class<?> contextClass, String resourceName) {
        checkArgument(contextClass != null, "Expected non-null contextClass");
        checkNotEmpty(resourceName);

        URL url = getResource(contextClass, resourceName);
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param contextClass the class the resource is relative to
     * @param resourceName the resource name
     * @return the URL
     * @see com.google.common.io.Resources#getResource(Class, String)
     */
    public static URL getResource(Class<?> contextClass, String resourceName) {
        return com.google.common.io.Resources.getResource(contextClass, resourceName);
    }

    /**
     * @param loader       the class loader to use
     * @param resourceName the resource name
     * @return the input stream
     */
    public static URL getResource(ClassLoader loader, String resourceName) {
        URL url = loader.getResource(resourceName);
        checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
    }

    /**
     * Returns current thread context class loader or {@link Resources} class loader
     *
     * @return the context class loader
     */
    public static ClassLoader getContextClassLoader() {
        return getContextClassLoader(Resources.class);
    }

    /**
     * Returns current thread context class loader or context class's class loader
     *
     * @param contextClass context class to use
     * @return the context class loader
     */
    public static ClassLoader getContextClassLoader(Class<?> contextClass) {
        return Objects.firstNonNull(
                Thread.currentThread().getContextClassLoader(),
                contextClass.getClassLoader()
        );
    }
}
