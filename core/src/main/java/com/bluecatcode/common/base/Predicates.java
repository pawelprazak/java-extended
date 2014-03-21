package com.bluecatcode.common.base;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Predicates {

    public static final Predicate<String>        isEmptyString         = String::isEmpty;
    public static final Predicate<CharSequence>  isEmptyCharSequence   = chs -> chs.length() == 0;

    public static final Predicate<Optional>      isEmptyOptional       = o -> !o.isPresent();

    public static final Predicate<Stream>        isEmptyStream         = s -> s.iterator().hasNext();
    public static final Predicate<Iterable>      isEmptyIterable       = i -> i.iterator().hasNext();
    public static final Predicate<Collection>    isEmptyCollection     = Collection::isEmpty;
    public static final Predicate<Map>           isEmptyMap            = Map::isEmpty;

    public static final Predicate<Object[]>     isEmptyObjectArray  = a -> a.length == 0;
    public static final Predicate<boolean[]>    isEmptyBooleanArray = a -> a.length == 0;
    public static final Predicate<byte[]>       isEmptyByteArray    = a -> a.length == 0;
    public static final Predicate<char[]>       isEmptyCharArray    = a -> a.length == 0;
    public static final Predicate<short[]>      isEmptyShortArray   = a -> a.length == 0;
    public static final Predicate<int[]>        isEmptyIntArray     = a -> a.length == 0;
    public static final Predicate<long[]>       isEmptyLongArray    = a -> a.length == 0;
    public static final Predicate<float[]>      isEmptyFloatArray   = a -> a.length == 0;
    public static final Predicate<double[]>     isEmptyDoubleArray  = a -> a.length == 0;


    public static final Predicate<String> isValidURI = (uri) -> {
        try {
            new URI(uri);
        } catch (URISyntaxException ex) {
            return false;
        }
        return true;
    };

    public static final Predicate<String> isValidEmail = (email) -> {
        try {
            new InternetAddress(email).validate();
        } catch (AddressException ex) {
            return false;
        }
        return true;
    };

    private Predicates() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}
