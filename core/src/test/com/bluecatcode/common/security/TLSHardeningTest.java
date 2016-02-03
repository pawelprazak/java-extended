package com.bluecatcode.common.security;

import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.util.Arrays;
import java.util.List;

import static com.bluecatcode.common.security.KeyStores.loadKeyManagers;
import static com.bluecatcode.common.security.KeyStores.loadTrustManagers;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TLSHardeningTest {

    private KeyManager[] keyManagers;
    private TrustManager[] trustManagers;

    private String keyStorePath;
    private String keyStorePassword;

    private String trustStorePath;
    private String trustStorePassword;

    @Before
    public void setUp() {
//        keyStorePath = Resources.getResourceAsFilePath(String.class, "/certs/client.jks");
//        keyStorePassword = "123456";
//
//        trustStorePath = Resources.getResourceAsFilePath(String.class, "/certs/server.jks");
//        trustStorePassword = "123456";

        keyManagers = loadKeyManagers(keyStorePath, keyStorePassword);
        trustManagers = loadTrustManagers(trustStorePath, trustStorePassword);
    }

    @Test
    public void shouldSupportTLSCipherSuites() throws Exception {
        // given
        SSLContext sslContext = Protocols.createTlsContext(keyManagers, trustManagers);
        List<String> supportedCipherSuites = Arrays.asList(Protocols.DEFAULT_CIPHER_SUITES);

        // when
        String[] defaultCipherSuites = sslContext.getServerSocketFactory().getDefaultCipherSuites();
        List<String> defaultCipherSuitesList = Arrays.asList(defaultCipherSuites);

        System.out.println(format("Supported %d cipher suites %s", supportedCipherSuites.size(), supportedCipherSuites));
        System.out.println(format("Default %d cipher suites %s", defaultCipherSuitesList.size(), defaultCipherSuitesList));

        boolean containsAllCipherSuites = defaultCipherSuitesList.containsAll(supportedCipherSuites);

        // then
        assertThat(containsAllCipherSuites, is(true));
    }
}