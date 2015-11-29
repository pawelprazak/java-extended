package com.bluecatcode.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import static com.google.common.hash.Hashing.*;

/**
 * Provides factory methods for basic use cases of Guava Hashing
 * <p>
 * When hashing a String it is very important to get the encoding right, otherwise
 * the resulting hash value might be invalid or "unexpected".
 * </p>
 * @see com.google.common.hash.Hashing
 */
@Beta
public final class Hash {

    /**
     * Creates a UTF-8 encoded hash using the hash function
     *
     * @param input string to encode
     * @param function hash function to use
     * @return the hash code
     */
    public static HashCode newHash(String input, HashFunction function) {
        return function.hashString(input, Charsets.UTF_8);
    }

    /**
     * Creates a UTF-8 encoded md5 hash as string, as a two-digit unsigned
     * hexadecimal number in lower case.
     *
     * @param input string to encode
     * @return encoded string
     * @see com.google.common.hash.HashCode#toString()
     */
    public static String md5AsString(String input) {
        return newHash(input, md5()).toString();
    }

    /**
     * Creates a UTF-8 encoded sha1 hash as string, as a two-digit unsigned
     * hexadecimal number in lower case.
     *
     * @param input string to encode
     * @return encoded string
     * @see com.google.common.hash.HashCode#toString()
     */
    public static String sha1AsString(String input) {
        return newHash(input, sha1()).toString();
    }

    /**
     * Creates a UTF-8 encoded sha256 hash as string, as a two-digit unsigned
     * hexadecimal number in lower case.
     *
     * @param input string to encode
     * @return encoded string
     * @see com.google.common.hash.HashCode#toString()
     */
    public static String sha256AsString(String input) {
        return newHash(input, sha256()).toString();
    }

    /**
     * Creates a UTF-8 encoded sha512 hash as string, as a two-digit unsigned
     * hexadecimal number in lower case.
     *
     * @param input string to encode
     * @return encoded string
     * @see com.google.common.hash.HashCode#toString()
     */
    public static String sha512AsString(String input) {
        return newHash(input, sha512()).toString();
    }

    private Hash() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}