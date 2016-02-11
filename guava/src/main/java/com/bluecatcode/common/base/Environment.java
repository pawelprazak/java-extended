package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Strings.emptyToNull;
import static java.lang.String.format;

/**
 * Provides easy and safe access to environment variables, system properties, etc.
 */
@Beta
public class Environment {

    private static final Logger log = Logger.getLogger(Environment.class.getName());

    private Environment() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.net.InetAddress#getHostName()
     * @return the local host name
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
     * <p>
     * <code>
     * java.net.UnknownHostException: &lt;hostname&gt;: &lt;hostname&gt;
     * at java.net.InetAddress.getLocalHost(InetAddress.java:1425)
     * ...
     * </code>
     * </p>
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
     * @param name the environment variable name
     * @return the environment variable value
     */
    public static Optional<String> getEnvironmentVariable(String name) {
        return Optional.fromNullable(
                emptyToNull(System.getenv(name))
        );
    }

    /**
     * @see System#getProperty(String)
     * @param name the system property name
     * @return the system property value
     */
    public static Optional<String> getSystemProperty(String name) {
        return Optional.fromNullable(
                emptyToNull(System.getProperty(name))
        );
    }

    /**
     * The minimum number of server port number.
     */
    public static final int MIN_PORT_NUMBER = 1;

    /**
     * The maximum number of server port number.
     */
    public static final int MAX_PORT_NUMBER = 49151;

    /**
     * Returns a currently available port number in the specified range.
     * @param fromPort the lower port limit
     * @param toPort the upper port limit
     * @throws IllegalArgumentException if port range is not between {@link #MIN_PORT_NUMBER}
     *      and {@link #MAX_PORT_NUMBER} or <code>fromPort</code> if greater than <code>toPort</code>.
     * @return an available port
     */
    public static int getNextAvailablePort(int fromPort, int toPort) {
        if (fromPort < MIN_PORT_NUMBER || toPort > MAX_PORT_NUMBER || fromPort > toPort) {
            throw new IllegalArgumentException(format("Invalid port range: %d ~ %d", fromPort, toPort));
        }

        int nextPort = fromPort;
        //noinspection StatementWithEmptyBody
        while (!isPortAvailable(nextPort++)) { /* Empty */ }
        return nextPort;
    }

    /**
     * Returns a currently available port number in range from the lower bound to {@link #MAX_PORT_NUMBER}.
     * @param fromPort the lower port limit
     * @return an available port
     */
    public static int getNextAvailablePort(int fromPort) {
        return getNextAvailablePort(fromPort, MAX_PORT_NUMBER);
    }

    /**
     * Checks if a port is available by opening a socket on it
     * @param port the port to test
     * @return true if the port is available
     */
    public static boolean isPortAvailable(int port) {
        StringBuilder builder = new StringBuilder();
        builder.append("Testing port ").append(port).append("\n");
        try (Socket s = new Socket("localhost", port)) {
            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            builder.append("Port ").append(port).append(" is not available").append("\n");
            return false;
        } catch (IOException e) {
            builder.append("Port ").append(port).append(" is available").append("\n");
            return true;
        } finally {
            log.fine(builder.toString());
        }
    }
}
