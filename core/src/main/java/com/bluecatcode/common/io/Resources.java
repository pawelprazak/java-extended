package com.bluecatcode.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.io.LineProcessor;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static com.bluecatcode.common.base.Preconditions.checkNotEmpty;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.readLines;

/**
 * Provides utility methods for working with resources in the classpath.
 *
 * @see com.google.common.io.Resources
 */
@Beta
public final class Resources {

    /**
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(URL, Charset)
     */
    public static String getResourceAsString(ClassLoader currentLoader, String resourceName) {
        checkNotNull(currentLoader);
        checkNotEmpty(resourceName);

        URL url = currentLoader.getResource(resourceName);
        checkArgument(url != null, "resource %s relative to class loader %s not found.", resourceName, currentLoader.getClass());
        //noinspection ConstantConditions
        return toString(url, Charsets.UTF_8);
    }

    /**
     * @see com.google.common.io.Resources#getResource(Class, String)
     * @see com.google.common.io.Resources#toString(URL, Charset)
     */
    public static String getResourceAsString(Class<?> contextClass, String resourceName) {
        checkNotNull(contextClass);
        checkNotEmpty(resourceName);

        URL url = getResource(contextClass, resourceName);
        return toString(url, Charsets.UTF_8);
    }

    /**
     * @see com.google.common.io.Resources#getResource(String)
     * @see com.google.common.io.Resources#toString(URL, Charset)
     */
    public static String getResourceAsString(String resourceName) {
        checkNotEmpty(resourceName);

        URL url = getResource(resourceName);
        return toString(url, Charsets.UTF_8);
    }

    /**
     * @see com.google.common.io.LineProcessor
     * @see com.google.common.io.Resources#getResource(String)
     * @see com.google.common.io.Resources#readLines(URL, Charset, LineProcessor)
     */
    public static <T> T getResourceWith(String resourceName, LineProcessor<T> lineProcessor) {
        checkNotEmpty(resourceName);
        checkNotNull(lineProcessor);

        URL url = getResource(resourceName);
        try {
            return readLines(url, Charsets.UTF_8, lineProcessor);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param resourceName  the resource name on class path
     * @return A {@link URL} object for reading the resource
     * @throws java.lang.IllegalArgumentException if the resource could not be found
     *      or the invoker doesn't have adequate privileges to get the resource
     * @see Resources#getDeepResource(ClassLoader, String)
     */
    public static URL getDeepResource(String resourceName) {
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
        URL url = getDeepResource(currentLoader, resourceName);
        checkArgument(url != null, "resource %s not found for the current thread context", resourceName);
        return url;
    }

    /**
     * @param contextClass class to use as the context for class loader
     * @param resourceName  the resource name on class path
     * @return A {@link URL} object for reading the resource
     * @throws java.lang.IllegalArgumentException if the resource could not be found
     *      or the invoker doesn't have adequate privileges to get the resource
     * @see Resources#getDeepResource(ClassLoader, String)
     */
    public static URL getDeepResource(Class<?> contextClass, String resourceName) {
        checkNotNull(contextClass);

        ClassLoader currentLoader = contextClass.getClassLoader();
        URL url = getDeepResource(currentLoader, resourceName);
        checkArgument(url != null,
                "resource %s not found for the context class %s", resourceName, contextClass.getCanonicalName());
        return url;
    }

    /**
     * Search class loader tree for resource, current implementation
     * attempts up to 10 levels of nested class loaders.
     *
     * @param startLoader class loader to start from
     * @param resourceName  the resource name on class path
     * @return A {@link URL} object for reading the resource
     * @throws java.lang.IllegalArgumentException if the resource could not be found
     *      or the invoker doesn't have adequate privileges to get the resource
     * @see java.lang.ClassLoader#getResource(String)
     */
    public static URL getDeepResource(ClassLoader startLoader, String resourceName) {
        checkNotNull(startLoader);
        checkNotEmpty(resourceName);

        ClassLoader currentLoader = startLoader;
        URL url = currentLoader.getResource(resourceName);
        int attempts = 0;
        while (url == null && attempts < 10) {  // search the class loader tree
            currentLoader = currentLoader.getParent();
            attempts += 1;
            //noinspection ConstantConditions
            if (currentLoader == null) {
                break;  // we are at the bootstrap class loader
            }
            url = currentLoader.getResource(resourceName);
        }
        checkArgument(url != null,
                "resource %s not found for the context class loader %s", resourceName, startLoader);
        return url;
    }

    /**
     * @see com.google.common.io.Resources#toString(URL, Charset)
     */
    public static String toString(URL url, Charset charset) {
        try {
            return com.google.common.io.Resources.toString(checkNotNull(url), charset);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Resources() {
        throw new UnsupportedOperationException("Private constructor");
    }
}
