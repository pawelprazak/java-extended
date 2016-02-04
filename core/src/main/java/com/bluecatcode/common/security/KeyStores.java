package com.bluecatcode.common.security;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;

import static java.lang.String.format;

/**
 * Key store related utility methods
 */
public class KeyStores {

    public static KeyStore loadKeyStore(String keyStorePath, String keyStorePassword) {
        return loadKeyStore(KeyStore.getDefaultType(), keyStorePath, keyStorePassword);
    }

    public static KeyStore loadKeyStore(String type, String path, String password) {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = new FileInputStream(path);
            KeyStore keyStore = KeyStore.getInstance(type);
            keyStore.load(resourceAsStream, password.toCharArray());
            return keyStore;
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException(format("Cannot load the key store: '%s'", path), e);
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    // safe to ignore
                }
            }
        }
    }

    public static KeyManager[] loadKeyManagers(String keyStorePath, String keyStorePassword) {
        return loadKeyManagers(KeyStore.getDefaultType(), keyStorePath, keyStorePassword);
    }

    public static KeyManager[] loadKeyManagers(String keyStoreType, String keyStorePath, String keyStorePassword) {
        KeyStore keyStore = KeyStores.loadKeyStore(keyStoreType, keyStorePath, keyStorePassword);
        String keyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        return loadKeyManagers(keyAlgorithm, keyStore, keyStorePassword);
    }

    public static KeyManager[] loadKeyManagers(String keyAlgorithm, KeyStore keyStore, String keyStorePassword) {
        KeyManagerFactory keyFactory;
        try {
            keyFactory = KeyManagerFactory.getInstance(keyAlgorithm);
            keyFactory.init(keyStore, keyStorePassword.toCharArray());
        } catch (UnrecoverableKeyException ex) {
            throw new IllegalArgumentException(ex);
        } catch (NoSuchAlgorithmException | KeyStoreException ex) {
            throw new IllegalStateException(ex);
        }
        return keyFactory.getKeyManagers();
    }

    public static TrustManager[] loadTrustManagers(String keyStorePath, String keyStorePassword) {
        return loadTrustManagers(KeyStore.getDefaultType(), keyStorePath, keyStorePassword);
    }

    public static TrustManager[] loadTrustManagers(String trustStoreType, String trustStorePath, String trustStorePassword) {
        KeyStore trustStore = KeyStores.loadKeyStore(trustStoreType, trustStorePath, trustStorePassword);
        String trustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        return loadTrustManagers(trustAlgorithm, trustStore);
    }

    public static TrustManager[] loadTrustManagers(String trustAlgorithm, KeyStore trustStore) {
        TrustManagerFactory trustFactory;
        try {
            trustFactory = TrustManagerFactory.getInstance(trustAlgorithm);
            trustFactory.init(trustStore);
        } catch (NoSuchAlgorithmException | KeyStoreException ex) {
            throw new IllegalStateException(ex);
        }
        return trustFactory.getTrustManagers();
    }

    public static KeyStore emptyKeyStore() {
        try {
            return KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    private KeyStores() {
        throw new UnsupportedOperationException();
    }
}
