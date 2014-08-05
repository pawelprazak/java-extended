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

public class Predicates2 {

    public static final Predicate<String>        isEmptyString         = new Predicate<String>() {
        @Override
        public boolean apply(@Nullable String t) {
            return t == null || t.isEmpty();
        }
    };

    public static final Predicate<CharSequence>  isEmptyCharSequence   = new Predicate<CharSequence>() {
        @Override
        public boolean apply(@Nullable CharSequence chs) {
            return chs == null || chs.length() == 0;
        }
    };

    public static final Predicate<Optional>      isEmptyOptional       = new Predicate<Optional>() {
        @Override
        public boolean apply(@Nullable Optional o) {
            return o == null || !o.isPresent();
        }
    };

    public static final Predicate<Iterable>      isEmptyIterable       = new Predicate<Iterable>() {
        @Override
        public boolean apply(@Nullable Iterable i) {
            return i == null || !i.iterator().hasNext();
        }
    };

    public static final Predicate<Collection>    isEmptyCollection     = new Predicate<Collection>() {
        @Override
        public boolean apply(@Nullable Collection c) {
            return c == null || c.isEmpty();
        }
    };

    public static final Predicate<Map>           isEmptyMap            = new Predicate<Map>() {
        @Override
        public boolean apply(@Nullable Map m) {
            return m == null || m.isEmpty();
        }
    };

    public static final Predicate<Object[]>     isEmptyObjectArray  = new Predicate<Object[]>() {
        @Override
        public boolean apply(@Nullable Object[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<boolean[]>    isEmptyBooleanArray = new Predicate<boolean[]>() {
        @Override
        public boolean apply(@Nullable boolean[] a) {
            return a == null || a.length == 0;
        }
    };
    public static final Predicate<byte[]>       isEmptyByteArray    = new Predicate<byte[]>() {
        @Override
        public boolean apply(@Nullable byte[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<char[]>       isEmptyCharArray    = new Predicate<char[]>() {
        @Override
        public boolean apply(@Nullable char[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<short[]>      isEmptyShortArray   = new Predicate<short[]>() {
        @Override
        public boolean apply(@Nullable short[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<int[]>        isEmptyIntArray     = new Predicate<int[]>() {
        @Override
        public boolean apply(@Nullable int[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<long[]>       isEmptyLongArray    = new Predicate<long[]>() {
        @Override
        public boolean apply(@Nullable long[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<float[]>      isEmptyFloatArray   = new Predicate<float[]>() {
        @Override
        public boolean apply(@Nullable float[] a) {
            return a == null || a.length == 0;
        }
    };

    public static final Predicate<double[]>     isEmptyDoubleArray  = new Predicate<double[]>() {
        @Override
        public boolean apply(@Nullable double[] a) {
            return a == null || a.length == 0;
        }
    };


    public static final Predicate<String> isValidURI = new Predicate<String>() {
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

    public static final Predicate<String> isValidEmail = new Predicate<String>() {
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

    private Predicates2() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}
