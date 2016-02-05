package com.bluecatcode.common.base;

import com.bluecatcode.common.base.predicates.IsInstancePredicate;
import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

import static java.lang.String.format;

/**
 * Additional Predicates as an extension to {@link com.google.common.base.Predicates}
 * <p>
 * In performance critical contexts it is probably better to use hand-written checks, but <b>measure first!</b>.
 * </p>
 * <ul>
 *     <li>nor - A 'not or' predicate</li>
 *     <li>isEmpty* - Performs emptiness and nullness check for:
 *          String, CharSequence, Optional, Collection, Iterable, Map, Object[], prim[]</li>
 *     <li>isValidURI - Performs URI check against RFC 2396 specification</li>
 *     <li>isValidEmail - Performs email address check against RFC 822 specification</li>
 *     <li>isInstance - Performs a runtime check if the reference is an instance of any of provided class(es)</li>
 * </ul>
 *
 * @see com.google.common.base.Predicates
 */
public final class Predicates {

    private static Predicate<IsEmpty> isEmpty() {
        return IsEmpty::isEmpty;
    }

    public static Predicate<String> isEmptyString() {
        return String::isEmpty;
    }

    public static Predicate<CharSequence> isEmptyCharSequence() {
        return chs -> chs.length() == 0;
    }

    public static Predicate<Optional> isEmptyOptional() {
        return o -> !o.isPresent();
    }

    public static Predicate<Iterable> isEmptyIterable() {
        return i -> !i.iterator().hasNext();
    }

    public static Predicate<Collection> isEmptyCollection() {
        return Collection::isEmpty;
    }

    public static Predicate<Map> isEmptyMap() {
        return Map::isEmpty;
    }

    public static Predicate<Object[]> isEmptyObjectArray() {
        return a -> a.length == 0;
    }

    public static  Predicate<boolean[]> isEmptyBooleanArray() {
        return a -> a.length == 0;
    }

    public static Predicate<byte[]> isEmptyByteArray() {
        return a -> a.length == 0;
    }

    public static Predicate<char[]> isEmptyCharArray() {
        return a -> a.length == 0;
    }

    public static Predicate<short[]> isEmptyShortArray() {
        return a -> a.length == 0;
    }

    public static Predicate<int[]> isEmptyIntArray() {
        return a -> a.length == 0;
    }

    public static Predicate<long[]> isEmptyLongArray() {
        return a -> a.length == 0;
    }

    public static Predicate<float[]> isEmptyFloatArray() {
        return a -> a.length == 0;
    }

    public static Predicate<double[]> isEmptyDoubleArray() {
        return a -> a.length == 0;
    }

    public static Predicate<Dictionary> isEmptyDictionary() {
        return Dictionary::isEmpty;
    }

    public static Predicate<Object> isEmptyObject() {
        return reference -> {
            //noinspection ConstantConditions
            if (reference == null) {
                return true;
            }

            Predicate<?> isEmpty;
            if (reference instanceof IsEmpty) {
                isEmpty = isEmpty();
            } else if (reference instanceof String) {
                isEmpty = isEmptyString();
            } else if (reference instanceof Optional) {
                isEmpty = isEmptyOptional();
            } else if (reference instanceof Collection) {
                isEmpty = isEmptyCollection();
            } else if (reference instanceof Iterable) {
                isEmpty = isEmptyIterable();
            } else if (reference instanceof Map) {
                isEmpty = isEmptyMap();
            } else if (reference instanceof Dictionary) {
                isEmpty = isEmptyDictionary();
            } else if (reference instanceof CharSequence) {
                isEmpty = isEmptyCharSequence();
            } else if (reference instanceof Object[]) {
                isEmpty = isEmptyObjectArray();
            } else if (reference instanceof boolean[]) {
                isEmpty = isEmptyBooleanArray();
            } else if (reference instanceof byte[]) {
                isEmpty = isEmptyByteArray();
            } else if (reference instanceof short[]) {
                isEmpty = isEmptyShortArray();
            } else if (reference instanceof char[]) {
                isEmpty = isEmptyCharArray();
            } else if (reference instanceof int[]) {
                isEmpty = isEmptyIntArray();
            } else if (reference instanceof long[]) {
                isEmpty = isEmptyLongArray();
            } else if (reference instanceof float[]) {
                isEmpty = isEmptyFloatArray();
            } else if (reference instanceof double[]) {
                isEmpty = isEmptyDoubleArray();
            } else {
                throw new IllegalArgumentException(format(
                        "Expected a supported type instead of %s, supported types: %s",
                        reference.getClass().getCanonicalName(),
                        "String, CharSequence, Optional, Iterable, Collection, Map, Object[], primitive[]"));
            }
            //noinspection unchecked
            return ((Predicate<Object>) isEmpty).apply(reference);
        };
    }

    /**
     * NOT OR Predicate
     *
     * Truth table:
     * <pre>
     * B  A  Q
     * -------
     * 0  0  1
     * 0  1  0
     * 1  0  0
     * 1  1  0
     * </pre>
     *
     * @param first the first argument
     * @param second the second argument
     * @return the predicate
     */
    @Beta
    public static <T> Predicate<T> nor(Predicate<? super T> first, Predicate<? super T> second) {
        return com.google.common.base.Predicates.<T>not(com.google.common.base.Predicates.<T>or(first, second));
    }

    @Beta
    @SafeVarargs
    public static <T> Predicate<T> nor(Predicate<? super T>... components) {
        return com.google.common.base.Predicates.<T>not(com.google.common.base.Predicates.<T>or(components));
    }

    /**
     * NOT AND Predicate
     *
     * Truth table:
     * <pre>
     * B  A  Q
     * -------
     * 0  0  1
     * 0  1  1
     * 1  0  1
     * 1  1  0
     * </pre>
     *
     * @param first the first argument
     * @param second the second argument
     * @return the predicate
     */
    @Beta
    public static <T> Predicate<T> nand(Predicate<? super T> first, Predicate<? super T> second) {
        return com.google.common.base.Predicates.<T>not(com.google.common.base.Predicates.<T>and(first, second));
    }

    /**
     * NOT AND Predicate
     *
     * @see #nand(com.google.common.base.Predicate, com.google.common.base.Predicate)
     * @param components the operator arguments
     * @return the predicate
     */
    @Beta
    @SafeVarargs
    public static <T> Predicate<T> nand(Predicate<? super T>... components) {
        return com.google.common.base.Predicates.<T>not(com.google.common.base.Predicates.<T>and(components));
    }

    @Beta
    public static Predicate<String> isValidURI() {
        return uri -> {
            //noinspection ConstantConditions
            if (uri == null) return false;
            try {
                new URI(uri);
            } catch (URISyntaxException ex) {
                return false;
            }
            return true;
        };
    }

    @Beta
    public static Predicate<String> isValidEmail() {
        return email -> {
            //noinspection ConstantConditions
            if (email == null) {
                return false;
            }
            try {
                new InternetAddress(email).validate();
            } catch (AddressException ex) {
                return false;
            }
            return true;
        };
    }

    @Beta
    public static <T> Predicate<T> isInstance(Class... types) {
        return new IsInstancePredicate<T>(types);
    }

    private Predicates() {
        throw new UnsupportedOperationException();
    }

}
