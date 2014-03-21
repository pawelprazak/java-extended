package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.bluecatcode.common.base.Predicates.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Splitter.on;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

/**
 * Additional Preconditions as an extension to {@link com.google.common.base.Preconditions}
 * <p/>
 * In performance critical contexts it is probably better to use hand-written checks, but <b>measuer first!</b>.
 * <ul>
 *     <li>check - Performs check with a predicate</li>
 *     <li>checkNotEmpty - Performs emptiness and nullness check for:
 *     String, CharSequence, Optional, Collection, Iterable, Map, Object[], prim[]</li>
 *     <li>checkMatches - Performs check against a regular expression or a hamcrest matcher</li>
 * </ul>
 *
 * @see com.google.common.base.Preconditions
 */
public final class Preconditions {

    public static final Object[] EMPTY_ERROR_MESSAGE_ARGS = new Object[]{};

    /**
     * Performs check with the predicate.
     *
     * @param reference reference to check
     * @param predicate the regular expression pattern
     * @param errorMessageTemplate a template for the exception message should the
     *     check fail. The message is formed by replacing each {@code %s}
     *     placeholder in the template with an argument. These are matched by
     *     position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *     Unmatched arguments will be appended to the formatted message in square
     *     braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message
     *     template. Arguments are converted to strings using
     *     {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided predicate
     * @throws NullPointerException if the {@code reference} or {@code predicate} is null
     * @see java.util.regex.Pattern
     */
    public static <T> T check(T reference, Predicate<T> predicate,
                                     @Nullable String errorMessageTemplate,
                                     @Nullable Object... errorMessageArgs) {
        checkNotNull(predicate, errorMessageTemplate, errorMessageArgs);
        checkNotNull(reference, errorMessageTemplate, errorMessageArgs);
        checkArgument(predicate.test(reference), errorMessageTemplate, errorMessageArgs);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference reference to check
     * @param errorMessage the exception message to use if the check fails; will
     *     be converted to a string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T check(T reference, Predicate<T> predicate, @Nullable Object errorMessage) {
        checkNotNull(reference, "Expected not null object, got %s", reference);
        check(reference, predicate, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference String reference to check
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T check(T reference, Predicate<T> predicate) {
        checkNotNull(reference, "Expected not null object, got %s", reference);
        check(reference, predicate, "Expected to fulfill the predicate, got %s", reference);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     * <p/>
     * Supports the following types:
     * String, CharSequence, Optional, Stream, Iterable, Collection, Map, Object[], primitive[]
     *
     * @param reference reference to check
     * @param errorMessageTemplate a template for the exception message should the
     *     check fail. The message is formed by replacing each {@code %s}
     *     placeholder in the template with an argument. These are matched by
     *     position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *     Unmatched arguments will be appended to the formatted message in square
     *     braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message
     *     template. Arguments are converted to strings using
     *     {@link String#valueOf(Object)}.
     * @param <T> reference type
     * @return the checked reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     *     or the reference type is not supported
     * @throws NullPointerException if the {@code reference} is null
     *     or the check fails and either {@code errorMessageTemplate} or {@code errorMessageArgs} is null
     *     (don't let this happen)
     */
    public static <T> T checkNotEmpty(T reference,
                                      @Nullable String errorMessageTemplate,
                                      @Nullable Object... errorMessageArgs) {
        checkNotNull(reference, errorMessageTemplate, errorMessageArgs);
        Predicate<?> isEmpty;
        if (reference instanceof String) {
            isEmpty = isEmptyString;
        } else if (reference instanceof Optional) {
            isEmpty = isEmptyOptional;
        } else if (reference instanceof Collection) {
            isEmpty = isEmptyCollection;
        } else if (reference instanceof Iterable) {
            isEmpty = isEmptyIterable;
        } else if (reference instanceof Stream) {
            isEmpty = isEmptyStream;
        } else if (reference instanceof Map) {
            isEmpty = isEmptyMap;
        } else if (reference instanceof CharSequence) {
            isEmpty = isEmptyCharSequence;
        } else if (reference instanceof Object[]) {
            isEmpty = isEmptyObjectArray;
        } else if (reference instanceof boolean[]) {
            isEmpty = isEmptyBooleanArray;
        } else if (reference instanceof byte[]) {
            isEmpty = isEmptyByteArray;
        } else if (reference instanceof short[]) {
            isEmpty = isEmptyShortArray;
        } else if (reference instanceof char[]) {
            isEmpty = isEmptyCharArray;
        } else if (reference instanceof int[]) {
            isEmpty = isEmptyIntArray;
        } else if (reference instanceof long[]) {
            isEmpty = isEmptyLongArray;
        } else if (reference instanceof float[]) {
            isEmpty = isEmptyFloatArray;
        } else if (reference instanceof double[]) {
            isEmpty = isEmptyDoubleArray;
        } else {
            throw new IllegalArgumentException(format(
                    "Expected a supported type instead of %s, supported types: %s",
                    reference.getClass().getCanonicalName(),
                    "String, Optional, Stream, Iterable, Collection, Map, Object[], primitive[]"));
        }
        //noinspection unchecked
        check(reference, (Predicate<Object>) isEmpty.negate(), errorMessageTemplate, errorMessageArgs);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     *
     * @param reference reference to check
     * @param errorMessage the exception message to use if the check fails; will
     *     be converted to a string using {@link String#valueOf(Object)}
     * @return the checked reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T checkNotEmpty(T reference, @Nullable Object errorMessage) {
        checkNotNull(reference, "Expected not null object, got %s", reference);
        checkNotEmpty(reference, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     *
     * @param reference String reference to check
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @return the checked reference
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T checkNotEmpty(T reference) {
        checkNotNull(reference, "Expected not null object, got %s", reference);
        checkNotEmpty(reference, "Expected not empty object, got %s", reference);
        return reference;
    }

    /**
     * Performs check with the regular expression pattern.
     *
     * @param reference reference to check
     * @param pattern the regular expression pattern
     * @param errorMessageTemplate a template for the exception message should the
     *     check fail. The message is formed by replacing each {@code %s}
     *     placeholder in the template with an argument. These are matched by
     *     position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *     Unmatched arguments will be appended to the formatted message in square
     *     braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message
     *     template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @throws NullPointerException if the {@code reference} is null; also when the check fails and either
     *     {@code errorMessageTemplate} or {@code errorMessageArgs} is null (don't let this happen)
     * @see java.util.regex.Pattern
     */
    @Beta
    public static void checkMatches(String reference, Pattern pattern,
                                    @Nullable String errorMessageTemplate,
                                    @Nullable Object... errorMessageArgs) {
        checkNotNull(pattern, "Expected a non-null pattern");
        checkNotNull(reference, "Expected a non-null reference");
        checkArgument(pattern.matcher(reference).matches(), errorMessageTemplate, errorMessageArgs);
    }

    /**
     * Performs check with the regular expression pattern.
     *
     * @param reference reference to check
     * @param pattern the regular expression pattern
     * @param errorMessage the exception message to use if the check fails; will
     *     be converted to a string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @see Preconditions#checkMatches(String, Pattern, String, Object...)
     */
    @Beta
    public static void checkMatches(String reference, Pattern pattern,
                                    @Nullable Object errorMessage) {
        checkMatches(reference, pattern, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Performs check with the regular expression pattern.
     *
     * @param reference reference to check
     * @param pattern the regular expression pattern
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @see Preconditions#checkMatches(String, java.util.regex.Pattern, String, Object...)
     */
    @Beta
    public static void checkMatches(String reference, Pattern pattern) {
        checkMatches(reference, pattern, "Expected %s to match %s", reference, pattern.pattern());
    }

    /**
     * Performs a runtime check if the reference is an instance of the class
     *
     * @throws IllegalArgumentException if the {@code reference} is not an instance of provided class {@code class_}
     * @throws NullPointerException if the {@code reference} is null; also when the check fails and either
     *     {@code errorMessageTemplate} or {@code errorMessageArgs} is null (don't let this happen)
     * @see Class#isInstance(java.lang.Object)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference,
                                        @Nullable String errorMessageTemplate,
                                        @Nullable Object... errorMessageArgs) {
        checkNotNull(class_, "Expected a non-null class_");
        checkNotNull(reference, "Expected a non-null reference");
        checkArgument(class_.isInstance(reference), errorMessageTemplate, errorMessageArgs);
        //noinspection unchecked
        return (T) reference;
    }

    /**
     * @see Preconditions#checkIsInstance(Class, Object, String, Object...)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference,
                                        @Nullable String errorMessage) {
        return checkIsInstance(class_, reference, errorMessage, EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * @see Preconditions#checkIsInstance(Class, Object, String, Object...)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference) {
        return checkIsInstance(class_, reference, "Expected reference to be instance of %s, got %s",
                class_.getName(), reference == null ? "null" : reference.getClass().getName());
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException if the {@code uri} is null
     * @see java.net.URI
     */
    public static String checkUri(String uri,
                                  @Nullable String errorMessageTemplate,
                                  @Nullable Object... errorMessageArgs) {
        checkNotEmpty(uri, "Expected non-null and non-empty uri, got %s", uri);
        return check(uri, isValidURI, errorMessageTemplate, errorMessageArgs);
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException if the {@code uri} is null
     * @see Preconditions#checkUri(String, String, Object...)
     */
    public static String checkUri(String uri, @Nullable Object errorMessage) {
        return check(uri, isValidURI, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException if the {@code uri} is null
     * @see Preconditions#checkUri(String, String, Object...)
     */
    public static String checkUri(String uri) {
        return check(uri, isValidURI, "Expected a valid URI, got %s", uri);
    }

    /**
     * Performs email address check against RFC 822 specification
     *
     * @param email an Email to check
     * @return checked Email
     * @throws IllegalArgumentException if the {@code email} is invalid
     */
    public static String checkEmail(String email) {
        return check(email, isValidEmail, email);
    }

    private static final String REGEXP_HOSTNAME = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])\\.?$";

    /**
     * Performs a hostname check against: RFC 952, RFC 1123 and RFC 1034
     *
     * @param hostname hostname to check
     * @return checked hostname
     * @throws IllegalArgumentException if the {@code hostname} is invalid
     * @throws NullPointerException     if the {@code hostname} is null
     */
    @Beta
    public static String checkHostname(String hostname,
                                       @Nullable String errorMessageTemplate,
                                       @Nullable Object... errorMessageArgs) {
        String message = messageFromNullable(errorMessageTemplate, errorMessageArgs, "; ");

        checkNotEmpty(hostname, "%sExpected non-null and non-empty hostname, got %s", message, hostname);
        checkArgument(hostname.length() > 0 && hostname.length() < 256,
                "%sExpected a hostname in range 1 to 255 characters, got %s", message, hostname.length());

        Iterable<String> labels = on('.').omitEmptyStrings().split(hostname);
        for (String label : labels) {
            checkArgument(label.length() > 0 && label.length() < 64,
                    "%sExpected a hostname label in range 1 to 63 characters, got %s", message, label.length());
        }

        checkMatches(hostname, compile(REGEXP_HOSTNAME),
                "%sExpected a hostname to match expression %s, got: %s", message, REGEXP_HOSTNAME, hostname);
        return hostname;
    }

    /**
     * @see Preconditions#checkHostname(String, String, Object...)
     */
    @Beta
    public static String checkHostname(String hostname) {
        return checkHostname(hostname, "", "");
    }

    /**
     * @see Preconditions#checkHostname(String, String, Object...)
     */
    @Beta
    public static String checkHostname(String hostname, @Nullable String errorMessageTemplate) {
        return checkHostname(hostname, errorMessageTemplate, EMPTY_ERROR_MESSAGE_ARGS);
    }

    @VisibleForTesting
    static String messageFromNullable(@Nullable String errorMessageTemplate, @Nullable Object[] errorMessageArgs) {
        return messageFromNullable(errorMessageTemplate, errorMessageArgs, "");
    }

    @VisibleForTesting
    static String messageFromNullable(@Nullable String errorMessageTemplate, @Nullable Object[] errorMessageArgs, @Nullable String separator) {
        return format(safeTemplate(errorMessageTemplate), errorMessageArgs) + (separator == null ? "" : separator);
    }

    private static String safeTemplate(@Nullable String errorMessageTemplate) {
        return (errorMessageTemplate == null || errorMessageTemplate.isEmpty()) ? "%s" : errorMessageTemplate;
    }

    private Preconditions() {
        throw new UnsupportedOperationException("Private constructor");
    }
}
