package com.bluecatcode.common.security;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;

import static java.lang.String.format;

/**
 * Key store utility class that holds an instance of a {@link KeyStore} and provides additional methods
 */
public class KeyProvider {

    private final KeyStore keyStore;

    /**
     * @param keyStoreFile full path to the key store file
     * @param keyStorePass password for the key store
     */
    public KeyProvider(String keyStoreFile, String keyStorePass) {
        this(KeyStore.getDefaultType(), keyStoreFile, keyStorePass);
    }

    /**
     * @param keyStoreType type of the key store
     * @param keyStoreFile full path to the key store file
     * @param keyStorePass password for the key store
     */
    public KeyProvider(String keyStoreType, String keyStoreFile, String keyStorePass) {
        this.keyStore = KeyStores.loadKeyStore(keyStoreType, keyStoreFile, keyStorePass);
    }

    /**
     * Gets a key from the key store
     * @param alias key alias
     * @param password key password
     * @return the key
     */
    public Key getKey(String alias, String password) {
        try {
            return keyStore.getKey(alias, password.toCharArray());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Gets a asymmetric encryption private key from the key store
     * @param alias key alias
     * @param password key password
     * @return the private key
     */
    public PrivateKey getPrivateKey(String alias, String password) {
        Key key = getKey(alias, password);
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        } else {
            throw new IllegalStateException(format("Key with alias '%s' was not a private key, but was: %s",
                    alias, key.getClass().getSimpleName()));
        }
    }

    /**
     * Gets a symmetric encryption secret key from the key store
     * @param alias key alias
     * @param password key password
     * @return the secret key
     */
    public SecretKey getSecretKey(String alias, String password) {
        Key key = getKey(alias, password);
        if (key instanceof PrivateKey) {
            return (SecretKey) key;
        } else {
            throw new IllegalStateException(format("Key with alias '%s' was not a secret key, but was: %s",
                    alias, key.getClass().getSimpleName()));
        }
    }

    /**
     * Gets the key store
     * @return the key store
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }
}