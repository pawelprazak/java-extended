package com.bluecatcode.common.base;

import com.bluecatcode.common.base.predicates.IsInstancePredicate;
import com.bluecatcode.common.interfaces.IsEmpty;
import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Predicates.*;
import static java.lang.String.format;

/**
 * Additional Predicates as an extension to {@link com.google.common.base.Predicates}
 * <p/>
 * In performance critical contexts it is probably better to use hand-written checks, but <b>measure first!</b>.
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
        return new Predicate<IsEmpty>() {
            @Override
            public boolean apply(@Nonnull IsEmpty o) {
                return o.isEmpty();
            }
        };
    }

    public static Predicate<String> isEmptyString() {
        return new Predicate<String>() {
            @Override
            public boolean apply(@Nonnull String t) {
                return t.isEmpty();
            }
        };
    }

    public static Predicate<CharSequence> isEmptyCharSequence() {
        return new Predicate<CharSequence>() {
            @Override
            public boolean apply(CharSequence chs) {
                return chs.length() == 0;
            }
        };
    }

    public static Predicate<Optional> isEmptyOptional() {
        return new Predicate<Optional>() {
            @Override
            public boolean apply(Optional o) {
                return !o.isPresent();
            }
        };
    }

    public static Predicate<Iterable> isEmptyIterable() {
        return new Predicate<Iterable>() {
            @Override
            public boolean apply(Iterable i) {
                return !i.iterator().hasNext();
            }
        };
    }

    public static Predicate<Collection> isEmptyCollection() {
        return new Predicate<Collection>() {
            @Override
            public boolean apply(Collection c) {
                return c.isEmpty();
            }
        };
    }

    public static Predicate<Map> isEmptyMap() {
        return new Predicate<Map>() {
            @Override
            public boolean apply(Map m) {
                return m.isEmpty();
            }
        };
    }

    public static Predicate<Object[]> isEmptyObjectArray() {
        return new Predicate<Object[]>() {
            @Override
            public boolean apply(Object[] a) {
                return a.length == 0;
            }
        };
    }

    public static  Predicate<boolean[]> isEmptyBooleanArray() {
        return new Predicate<boolean[]>() {
            @Override
            public boolean apply(boolean[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<byte[]> isEmptyByteArray() {
        return new Predicate<byte[]>() {
            @Override
            public boolean apply(byte[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<char[]> isEmptyCharArray() {
        return new Predicate<char[]>() {
            @Override
            public boolean apply(char[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<short[]> isEmptyShortArray() {
        return new Predicate<short[]>() {
            @Override
            public boolean apply(short[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<int[]> isEmptyIntArray() {
        return new Predicate<int[]>() {
            @Override
            public boolean apply(int[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<long[]> isEmptyLongArray() {
        return new Predicate<long[]>() {
            @Override
            public boolean apply(long[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<float[]> isEmptyFloatArray() {
        return new Predicate<float[]>() {
            @Override
            public boolean apply(float[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<double[]> isEmptyDoubleArray() {
        return new Predicate<double[]>() {
            @Override
            public boolean apply(double[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<Object> isEmptyObject() {
        return new Predicate<Object>() {
            @Override
            public boolean apply(@Nullable Object reference) {
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
            }
        };
    }

    /**
     * Truth table:
     * <pre>
     * B  A  Q
     * -------
     * 0  0  1
     * 0  1  0
     * 1  0  0
     * 1  1  0
     * </pre>
     */
    @Beta
    public static <T> Predicate<T> nor(Predicate<? super T> first, Predicate<? super T> second) {
        return not(or(first, second));
    }

    @Beta
    @SafeVarargs
    public static <T> Predicate<T> nor(Predicate<? super T>... components) {
        return not(or(components));
    }

    /**
     * Truth table:
     * <pre>
     * B  A  Q
     * -------
     * 0  0  1
     * 0  1  1
     * 1  0  1
     * 1  1  0
     * </pre>
     */
    @Beta
    public static <T> Predicate<T> nand(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
        return not(and(predicate1, predicate2));
    }

    /**
     * @see #nand(com.google.common.base.Predicate, com.google.common.base.Predicate)
     */
    @Beta
    @SafeVarargs
    public static <T> Predicate<T> nand(Predicate<? super T>... components) {
        return not(and(components));
    }

    @Beta
    public static Predicate<String> isValidURI() {
        return new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String uri) {
                if (uri == null) return false;

                try {
                    new URI(uri);
                } catch (URISyntaxException ex) {
                    return false;
                }
                return true;
            }
        };
    }

    @Beta
    public static Predicate<String> isValidEmail() {
        return new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String email) {
                if (email == null) return false;

                try {
                    new InternetAddress(email).validate();
                } catch (AddressException ex) {
                    return false;
                }
                return true;
            }
        };
    }

    @Beta
    public static <T> Predicate<T> isInstance(Class... types) {
        return new IsInstancePredicate<>(types);
    }

    private Predicates() {
        throw new UnsupportedOperationException();
    }

}
