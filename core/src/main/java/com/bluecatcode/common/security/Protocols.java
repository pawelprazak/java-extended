package com.bluecatcode.common.security;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Protocols {

    public static final String TLS = "TLS";
    public static final String TLS_V1 = "TLSv1";
    public static final String TLS_V1_1 = "TLSv1.1";
    public static final String TLS_V1_2 = "TLSv1.2";

    public static SSLContext createSslContext(KeyManager[] keyManagers, TrustManager[] trustManagers) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            String[] enabledProtocols = {
                    TLS_V1_2,
                    TLS_V1_1,
                    TLS_V1
            };
            sslContext.getDefaultSSLParameters().setProtocols(enabledProtocols);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        } catch (KeyManagementException ex) {
            throw new IllegalStateException(ex);
        }
        return sslContext;
    }

    private Protocols() {
        throw new UnsupportedOperationException();
    }
}
