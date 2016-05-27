package com.bluecatcode.common.security;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Each named cipher suite, e.g. TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
 * - defines a Key Exchange Algorithms,
 * - a bulk Encryption Algorithms,
 * - a Message Authentication Code (MAC) algorithm,
 * - and a pseudorandom function (PRF)
 *
 * OWASP Rules:
 * - Only Support Strong Protocols (TLS 1.2, TLS 1.1, avoid TLS 1.0 if possible)
 * - Highest Priority for Ephemeral Key Exchanges
 * - Favor DHE over ECDHE
 * - Use RSA-Keys (no DSA/DSS)
 * - Favor GCM over CBC, use Authenticated Encryption with Associated Data (AEAD), e.g. AES-GCM, AES-CCM
 * - Priorize the ciphers by the sizes of the Cipher and the MAC
 * - Use SHA1 or above for digests, prefer SHA2 (or equivalent)
 * - Ciphers should be usable for DH-Pamameters >= 2048 bits, without blocking legacy browsers (The cipher ‘DHE-RSA-AES128-SHA’ is suppressed as some browsers like to use it but are not capable to cope with DH-Params > 1024 bits.)
 * - candidate for minimal best compromise is TLS_RSA_WITH_3DES_EDE_CBC_SHA (0xa)
 *
 * Best Key Exchange Algorithms (Perfect Forward Secrecy):
 * - DHE-RSA
 * - ECDHE-RSA
 * - ECDHE-ECDSA
 *
 * Best Encryption Algorithms:
 * - AES-GCM (block)
 * - AES-CCM (block)
 * - ChaCha20-Poly1305 (stream)
 *
 * Best Message Authentication Codes (MAC):
 * - AEAD (used for GCM mode and CCM mode)
 * - HMAC-SHA256/384 (used for CBC and stream ciphers)
 *
 * Weak ciphers such as DES and RC4 should be disabled.
 *
 * Ideally, HTTPS service would enable only TLS 1.2 and enable only AEAD (Authenticated Encryption with Associated Data)
 * based cipher suites with SHA-2, 4096 bit DH parameters and 512 bit EC curves of a type that matches your requirements
 * (government approved or not government generated).
 *
 * Look at the java.security file properties, e.g.:
 * jdk.tls.client.protocols=TLSv1.2
 * jdk.tls.disabledAlgorithms=MD5, SHA1, RC4, DSA, RSA keySize < 2048, SSLv3
 * jdk.tls.ephemeralDHKeySize=2048
 * jdk.certpath.disabledAlgorithms=MD2, DSA, RSA keySize < 2048
 *
 * @see <a href="https://en.wikipedia.org/wiki/Transport_Layer_Security">Transport Layer Security</a>
 * @see <a href="https://www.owasp.org/index.php/Transport_Layer_Protection_Cheat_Sheet">Transport Layer Protection Cheat Sheet</a>
 * @see <a href="https://wiki.mozilla.org/Security/Server_Side_TLS">Server Side TLS</a>
 * @see <a href="https://www.openssl.org/docs/apps/ciphers.html">OpenSSL Ciphers</a>
 * @see <a href="http://security.stackexchange.com/questions/76993/now-that-it-is-2015-what-ssl-tls-cipher-suites-should-be-used-in-a-high-securit/77018#77018">Secure TLS Cipher Suites</a>
 */
public class Protocols {

    public static final String HTTPS_SCHEMA = "https";

    public static final String TLS = "TLS";
    public static final String TLS_V1 = "TLSv1";
    public static final String TLS_V1_1 = "TLSv1.1";
    public static final String TLS_V1_2 = "TLSv1.2";

    public static final String[] DEFAULT_TLS_PROTOCOLS = {
            TLS_V1_2,
            TLS_V1_1,
            TLS_V1
    };

    /**
     * TLS 1.2 AEAD only (all are SHA-2 as well)
     */
    public static final String[] STRONGEST_CIPHER_SUITES = new String[]{
            "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",           // (0x9f)
            "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",           // (0x9e)
            "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",     // (0xcc15)
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",         // (0xc030)
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",         // (0xc02f)
            "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",   // (0xcc13)
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",       // (0xc02c)
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",       // (0xc02b)
            "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256", // (0xcc14)
    };

    /**
     * TLS 1.2 SHA2 family (using the much older CBC mode)
     */
    public static final String[] STRONG_CIPHER_SUITES = new String[]{
            "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256",        // (0x6b)
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",        // (0x67)
            "TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256",   // (0xc4)
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",      // (0xc028)
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",      // (0xc027)
            "TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384", // (0xc077)
            "TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256", // (0xc076)
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",    // (0xc024)
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",    // (0xc023)
    };

    /**
     * TLS 1.0 and 1.1 with modern ciphers (and outdated hashes, since that's all that's available)
     */
    public static final String[] ACCEPTABLE_CIPHER_SUITES = new String[]{
            "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",      // (0x39)
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",      // (0x33)
            "TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA", // (0x88)
            "TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA", // (0x45)
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",    // (0xc014)
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",    // (0xc013)
    };

    /**
     * TLS 1.0 and 1.1 with older but still reasonable ciphers and outdated hashes
     */
    public static final String[] MINIMAL_CIPHER_SUITES = new String[]{
            "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA",     // (0x16)
            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",   // (0xc012)
            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", // (0xc008)
    };

    /**
     * List of sensible cipher suites for TLS Cipher hardening.
     * Versions: TLSv1, TLSv1.1, TLSv1.2
     */
    public static final String[] DEFAULT_CIPHER_SUITES = combine(
            STRONGEST_CIPHER_SUITES,
            STRONG_CIPHER_SUITES,
            ACCEPTABLE_CIPHER_SUITES,
            MINIMAL_CIPHER_SUITES
    );

    private static String[] combine(String[]... arrays) {
        List<String> list = new ArrayList<>();
        for (String[] array : arrays) {
            Collections.addAll(list, array);
        }
        return list.toArray(new String[list.size()]);
    }

    public static SSLContext createTlsContext(KeyManager[] keyManagers, TrustManager[] trustManagers) {
        return createTlsContext(keyManagers, trustManagers, DEFAULT_TLS_PROTOCOLS, DEFAULT_CIPHER_SUITES);
    }

    public static SSLContext createTlsContext(KeyManager[] keyManagers, TrustManager[] trustManagers, String[] enabledProtocols, String[] supportedCipherSuites) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            sslContext.getDefaultSSLParameters().setProtocols(enabledProtocols);
            sslContext.getDefaultSSLParameters().setCipherSuites(supportedCipherSuites);
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            throw new IllegalStateException(ex);
        }
        return sslContext;
    }

    public static HttpsURLConnection httpsConnection(URL url, HttpsConnectionInitializer initializer) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("Expected non-null url");
        }
        if (url.getProtocol().equalsIgnoreCase(HTTPS_SCHEMA)) {
            throw new IllegalArgumentException("Expected a HTTPS url");
        }
        if (initializer == null) {
            throw new IllegalArgumentException("Expected non-null initializer");
        }
        URLConnection urlConnection = url.openConnection();
        if (!(urlConnection instanceof HttpsURLConnection)) {
            throw new IllegalStateException("Expected a HttpsURLConnection");
        }
        HttpsURLConnection connection = (HttpsURLConnection) urlConnection;
        connection = initializer.initialize(connection);
        if (connection == null) {
            throw new IllegalStateException("Expected non-null connection returned by the initializer");
        }
        return connection;
    }

    public interface HttpsConnectionInitializer {
        @Nonnull
        HttpsURLConnection initialize(@Nonnull HttpsURLConnection connection);
    }

    private Protocols() {
        throw new UnsupportedOperationException();
    }
}
