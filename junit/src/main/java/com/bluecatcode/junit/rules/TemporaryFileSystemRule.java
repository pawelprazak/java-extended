package com.bluecatcode.junit.rules;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.google.common.jimfs.JimfsFileSystemProvider;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/**
 * Virtual in-memory file system for JUnit
 * <p>
 * The URI will look like: {@code jimfs://test/foo/bar}
 */
public class TemporaryFileSystemRule extends ExternalResource {

    static final String SPI_DEFAULT_FILE_SYSTEM_PROVIDER = "java.nio.file.spi.DefaultFileSystemProvider";

    private final FileSystem fs;
    private final String defaultFileSystemProvider;

    public TemporaryFileSystemRule(Configuration configuration) {
        this.fs = Jimfs.newFileSystem("test", configuration);
        this.defaultFileSystemProvider = System.getProperty(SPI_DEFAULT_FILE_SYSTEM_PROVIDER);
    }

    @Override
    protected void before() throws Throwable {
        if (!fs.isOpen()) throw new AssertionError("Expected open virtual file system");
        if (fs.isReadOnly()) throw new AssertionError("Expected read-write virtual file system");

        System.setProperty(SPI_DEFAULT_FILE_SYSTEM_PROVIDER, JimfsFileSystemProvider.class.getCanonicalName());
    }

    @Override
    protected void after() {
        System.setProperty(SPI_DEFAULT_FILE_SYSTEM_PROVIDER, defaultFileSystemProvider);
        try {
            fs.close();
        } catch (IOException e) {
            // safe to ignore
        }
    }

    /**
     * @see FileSystem#getSeparator()
     */
    public String getSeparator() {
        return fs.getSeparator();
    }

    /**
     * @see FileSystem#newWatchService()
     */
    public WatchService newWatchService() throws IOException {
        return fs.newWatchService();
    }

    /**
     * @see FileSystem#getUserPrincipalLookupService()
     */
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return fs.getUserPrincipalLookupService();
    }

    /**
     * @see FileSystem#getPathMatcher(String)
     */
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return fs.getPathMatcher(syntaxAndPattern);
    }

    /**
     * @see FileSystem#getPath(String, String...)
     */
    public Path getPath(String first, String... more) {
        return fs.getPath(first, more);
    }

    /**
     * @see FileSystem#supportedFileAttributeViews()
     */
    public Set<String> supportedFileAttributeViews() {
        return fs.supportedFileAttributeViews();
    }

    /**
     * @see FileSystem#getFileStores()
     */
    public Iterable<FileStore> getFileStores() {
        return fs.getFileStores();
    }

    /**
     * @see FileSystem#getRootDirectories()
     */
    public Iterable<Path> getRootDirectories() {
        return fs.getRootDirectories();
    }

    /**
     * @see FileSystem#provider()
     */
    public FileSystemProvider provider() {
        return fs.provider();
    }

    /**
     * @see FileSystem#isReadOnly()
     */
    public boolean isReadOnly() {
        return fs.isReadOnly();
    }

    /**
     * @see FileSystem#isOpen()
     */
    public boolean isOpen() {
        return fs.isOpen();
    }
}