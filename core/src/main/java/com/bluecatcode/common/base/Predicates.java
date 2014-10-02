package com.bluecatcode.common.base;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;

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

    public static <T> Predicate<T> nor(Predicate<? super T> first, Predicate<? super T> second) {
        return not(or(first, second));
    }

    @SafeVarargs
    public static <T> Predicate<T> nor(Predicate<? super T>... components) {
        return not(or(components));
    }

    public static Predicate<String> isEmptyString() {
        return new NullablePredicate<String>() {
            @Override
            public boolean applyNotNull(String t) {
                return t.isEmpty();
            }
        };
    }

    public static Predicate<CharSequence> isEmptyCharSequence() {
        return new NullablePredicate<CharSequence>() {
            @Override
            public boolean applyNotNull(CharSequence chs) {
                return chs.length() == 0;
            }
        };
    }

    public static Predicate<Optional> isEmptyOptional() {
        return new NullablePredicate<Optional>() {
            @Override
            public boolean applyNotNull(Optional o) {
                return !o.isPresent();
            }
        };
    }

    public static Predicate<Iterable> isEmptyIterable() {
        return new NullablePredicate<Iterable>() {
            @Override
            public boolean applyNotNull(Iterable i) {
                return !i.iterator().hasNext();
            }
        };
    }

    public static Predicate<Collection> isEmptyCollection() {
        return new NullablePredicate<Collection>() {
            @Override
            public boolean applyNotNull(Collection c) {
                return c.isEmpty();
            }
        };
    }

    public static Predicate<Map> isEmptyMap() {
        return new NullablePredicate<Map>() {
            @Override
            public boolean applyNotNull(Map m) {
                return m.isEmpty();
            }
        };
    }

    public static Predicate<Object[]> isEmptyObjectArray() {
        return new NullablePredicate<Object[]>() {
            @Override
            public boolean applyNotNull(Object[] a) {
                return a.length == 0;
            }
        };
    }

    public static  Predicate<boolean[]> isEmptyBooleanArray() {
        return new NullablePredicate<boolean[]>() {
            @Override
            public boolean applyNotNull(boolean[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<byte[]> isEmptyByteArray() {
        return new NullablePredicate<byte[]>() {
            @Override
            public boolean applyNotNull(byte[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<char[]> isEmptyCharArray() {
        return new NullablePredicate<char[]>() {
            @Override
            public boolean applyNotNull(char[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<short[]> isEmptyShortArray() {
        return new NullablePredicate<short[]>() {
            @Override
            public boolean applyNotNull(short[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<int[]> isEmptyIntArray() {
        return new NullablePredicate<int[]>() {
            @Override
            public boolean applyNotNull(int[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<long[]> isEmptyLongArray() {
        return new NullablePredicate<long[]>() {
            @Override
            public boolean applyNotNull(long[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<float[]> isEmptyFloatArray() {
        return new NullablePredicate<float[]>() {
            @Override
            public boolean applyNotNull(float[] a) {
                return a.length == 0;
            }
        };
    }

    public static Predicate<double[]> isEmptyDoubleArray() {
        return new NullablePredicate<double[]>() {
            @Override
            public boolean applyNotNull(double[] a) {
                return a.length == 0;
            }
        };
    }

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

    public static <T> Predicate<T> isInstance(Class... types) {
        return new IsInstancePredicate<T>(types);
    }

    private Predicates() {
        throw new UnsupportedOperationException("Private constructor");
    }
}
