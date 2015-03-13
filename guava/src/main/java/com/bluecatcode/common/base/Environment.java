package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Strings.emptyToNull;

/**
 * Provides easy and safe access to environment variables, system properties
 */
@Beta
public class Environment {

    private static final Logger log = Logger.getLogger(Environment.class.getName());

    /**
     * @see java.net.InetAddress#getHostName()
     */
    public static Optional<String> getLocalHostName() {
        try {
            String hostName = getLocalHostNameWithRecovery();
            return Optional.fromNullable(hostName);
        } catch (UnknownHostException e) {
            log.log(Level.FINE, "Cannot get localhost name", e);
            return Optional.absent();
        }
    }

    /**
     * When using the {@link java.net.InetAddress#getHostName()} method in an
     * environment where neither a proper DNS lookup nor an <tt>/etc/hosts</tt>
     * entry exists for a given host, the following exception will be thrown:
     * <p/>
     * <code>
     * java.net.UnknownHostException: &lt;hostname&gt;: &lt;hostname&gt;
     * at java.net.InetAddress.getLocalHost(InetAddress.java:1425)
     * ...
     * </code>
     * <p/>
     * Instead of just throwing an UnknownHostException and giving up, this
     * method grabs a suitable hostname from the exception and prevents the
     * exception from being thrown. If a suitable hostname cannot be acquired
     * from the exception, only then is the <tt>UnknownHostException</tt> thrown.
     *
     * @return the hostname
     * @throws java.net.UnknownHostException is thrown if hostname could not be resolved
     */
    private static String getLocalHostNameWithRecovery() throws UnknownHostException {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException exception) {
            String host = exception.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            throw exception;
        }
    }

    /**
     * @see System#getenv(String)
     */
    public static Optional<String> getEnvironmentVariable(String name) {
        return Optional.fromNullable(
                emptyToNull(System.getenv(name))
        );
    }

    /**
     * @see System#getProperty(String)
     */
    public static Optional<String> getSystemProperty(String name) {
        return Optional.fromNullable(
                emptyToNull(System.getProperty(name))
        );
    }

    private Environment() {
        throw new UnsupportedOperationException();
    }
}
