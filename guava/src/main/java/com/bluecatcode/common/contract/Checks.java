package com.bluecatcode.common.contract;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

import static com.bluecatcode.common.exceptions.Exceptions.*;
import static com.bluecatcode.common.predicates.Predicates.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Splitter.on;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

/**
 * Use these methods to throw recoverable exceptions if validation fails.
 * Recoverable exceptions are exceptions that a correct program can and should catch and recover from.
 * (Recoverable exceptions may be checked or unchecked.)
 * <p>
 * Checks is an extension to {@link com.google.common.base.Preconditions}
 * <p>
 * In performance critical contexts it is probably better to use hand-written checks, but <b>measure first!</b>.
 * </p>
 * <ul>
 * <li>check - Performs check with a predicate</li>
 * <li>checkNotEmpty - Performs emptiness and nullness check for:
 * String, CharSequence, Optional, Collection, Iterable, Map, Object[], prim[]</li>
 * <li>checkMatches - Performs check against a regular expression or a hamcrest matcher</li>
 * </ul>
 *
 * @see com.google.common.base.Preconditions
 */
public final class Checks {

    public static final Object[] EMPTY_ERROR_MESSAGE_ARGS = new Object[]{};

    private static final String REGEXP_HOSTNAME = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])\\.?$";

    /**
     * Performs check of the condition.
     *
     * @param condition condition to check
     * @param <E>       the exception type
     * @throws IllegalArgumentException if the {@code throwable} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <E extends Throwable> void check(boolean condition, E throwable) throws E {
        checkArgument(throwable != null, "Expected non-null reference");
        if (!condition) {
            throw throwable;
        }
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference reference to check
     * @param predicate the predicate to use
     * @param throwable the throwable to throw
     * @param <T>       the reference type
     * @param <E>       the exception type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference}, {@code predicate} or {@code throwableType} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <T, E extends Throwable> T check(T reference, Predicate<T> predicate, E throwable) throws E {
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(predicate != null, "Expected non-null predicate");
        check(predicate.apply(reference), throwable);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference reference to check
     * @param predicate the predicate to use
     * @param throwable the throwable to throw
     * @param <T>       the reference type
     * @param <E>       the exception type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference}, {@code predicate} or {@code throwableType} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <T, E extends Throwable> T check(T reference, Predicate<T> predicate, Supplier<E> throwable) throws E {
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(predicate != null, "Expected non-null predicate");
        checkArgument(throwable != null, "Expected non-null throwable supplier");
        check(reference, predicate, throwable.get());
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference            reference to check
     * @param predicate            the predicate to use
     * @param throwableType        the throwable type to throw
     * @param errorMessageTemplate a template for the exception message should the
     *                             check fail. The message is formed by replacing each {@code %s}
     *                             placeholder in the template with an argument. These are matched by
     *                             position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *                             Unmatched arguments will be appended to the formatted message in square
     *                             braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message
     *                             template. Arguments are converted to strings using
     *                             {@link String#valueOf(Object)}.
     * @param <T>                  the reference type
     * @param <E>                  the exception type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference}, {@code predicate} or {@code throwableType} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <T, E extends Throwable> T check(T reference, Predicate<T> predicate,
                                                   Class<E> throwableType,
                                                   @Nullable String errorMessageTemplate,
                                                   @Nullable Object... errorMessageArgs) throws E {
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(predicate != null, "Expected non-null predicate");
        checkArgument(throwableType != null, "Expected non-null throwableType");
        check(reference, predicate, (Supplier<E>) () -> throwable(
                throwableType, parameters(String.class),
                arguments(messageFromNullable(errorMessageTemplate, errorMessageArgs)))
        );
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference     reference to check
     * @param predicate     the predicate to use
     * @param throwableType the throwable type to throw
     * @param errorMessage  the exception message to use if the check fails; will
     *                      be converted to a string using {@link String#valueOf(Object)}
     * @param <T>           the reference type
     * @param <E>           the exception type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference}, {@code predicate} or {@code throwableType} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <T, E extends Throwable> T check(T reference, Predicate<T> predicate,
                                                   Class<E> throwableType,
                                                   @Nullable String errorMessage) throws E {
        check(reference, predicate, throwableType, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference     reference to check
     * @param predicate     the predicate to use
     * @param throwableType the throwable type to throw
     * @param <T>           the reference type
     * @param <E>           the exception type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference}, {@code predicate} or {@code throwableType} is null
     * @throws IllegalArgumentException if the {@code throwableType} cannot be instantiated
     * @throws E                        if the {@code reference} doesn't match provided predicate
     */
    @Beta
    public static <T, E extends Throwable> T check(T reference, Predicate<T> predicate,
                                                   Class<E> throwableType) throws E {
        check(reference, predicate, throwableType, "Expected to fulfill the predicate, got %s", reference);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference            reference to check
     * @param predicate            the predicate to use
     * @param errorMessageTemplate a template for the exception message should the
     *                             check fail. The message is formed by replacing each {@code %s}
     *                             placeholder in the template with an argument. These are matched by
     *                             position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *                             Unmatched arguments will be appended to the formatted message in square
     *                             braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message
     *                             template. Arguments are converted to strings using
     *                             {@link String#valueOf(Object)}.
     * @param <T>                  the reference type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference} or {@code predicate} is null
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided predicate
     */
    public static <T> T check(T reference, Predicate<T> predicate,
                              @Nullable String errorMessageTemplate,
                              @Nullable Object... errorMessageArgs) {
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(predicate != null, "Expected non-null predicate");
        checkArgument(predicate.apply(reference), messageFromNullable(errorMessageTemplate, errorMessageArgs));
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference    the reference to check
     * @param predicate    the predicate to use
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @param <T>          the reference type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T check(T reference, Predicate<T> predicate, @Nullable Object errorMessage) {
        check(reference, predicate, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
        return reference;
    }

    /**
     * Performs check with the predicate.
     *
     * @param reference the reference to check
     * @param predicate the predicate to use
     * @param <T>       the reference type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T check(T reference, Predicate<T> predicate) {
        check(reference, predicate, "Expected to fulfill the predicate, got %s", reference);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     * <p>
     * Supports the following types:
     * String, CharSequence, Optional, Stream, Iterable, Collection, Map, Object[], primitive[]
     * </p>
     *
     * @param reference            reference to check
     * @param errorMessageTemplate a template for the exception message should the
     *                             check fail. The message is formed by replacing each {@code %s}
     *                             placeholder in the template with an argument. These are matched by
     *                             position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *                             Unmatched arguments will be appended to the formatted message in square
     *                             braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message
     *                             template. Arguments are converted to strings using
     *                             {@link String#valueOf(Object)}.
     * @param <T>                  the reference type
     * @return the checked reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     *                                  or the reference type is not supported
     * @throws NullPointerException     if the {@code reference} is null
     *                                  or the check fails and either {@code errorMessageTemplate} or {@code errorMessageArgs} is null
     *                                  (don't let this happen)
     */
    public static <T> T checkNotEmpty(T reference,
                                      @Nullable String errorMessageTemplate,
                                      @Nullable Object... errorMessageArgs) {
        checkArgument(reference != null, "Expected non-null reference");
        check(reference, not(isEmptyObject()), errorMessageTemplate, errorMessageArgs);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     *
     * @param reference    reference to check
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @param <T>          the reference type
     * @return the checked reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T checkNotEmpty(T reference, @Nullable Object errorMessage) {
        checkNotEmpty(reference, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
        return reference;
    }

    /**
     * Performs emptiness and nullness check.
     *
     * @param reference String reference to check
     * @param <T>       the reference type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference} is empty
     * @throws NullPointerException     if the {@code reference} is null
     * @see #checkNotEmpty(Object, String, Object...)
     */
    public static <T> T checkNotEmpty(T reference) {
        return checkNotEmpty(reference, "Expected not empty object, got '%s'", reference);
    }

    /**
     * Performs check with the regular expression pattern.
     *
     * @param reference            reference to check
     * @param pattern              the regular expression pattern
     * @param errorMessageTemplate a template for the exception message should the
     *                             check fail. The message is formed by replacing each {@code %s}
     *                             placeholder in the template with an argument. These are matched by
     *                             position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *                             Unmatched arguments will be appended to the formatted message in square
     *                             braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message
     *                             template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @throws NullPointerException     if the {@code reference} is null; also when the check fails and either
     *                                  {@code errorMessageTemplate} or {@code errorMessageArgs} is null (don't let this happen)
     * @see java.util.regex.Pattern
     */
    @Beta
    public static void checkMatches(String reference, Pattern pattern,
                                    @Nullable String errorMessageTemplate,
                                    @Nullable Object... errorMessageArgs) {
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(pattern != null, "Expected non-null pattern");
        checkArgument(pattern.matcher(reference).matches(), errorMessageTemplate, errorMessageArgs);
    }

    /**
     * Performs check with the regular expression pattern.
     *
     * @param reference    reference to check
     * @param pattern      the regular expression pattern
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @see Checks#checkMatches(String, java.util.regex.Pattern, String, Object...)
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
     * @param pattern   the regular expression pattern
     * @throws IllegalArgumentException if the {@code reference} doesn't match provided regular expression
     * @see Checks#checkMatches(String, java.util.regex.Pattern, String, Object...)
     */
    @Beta
    public static void checkMatches(String reference, Pattern pattern) {
        checkMatches(reference, pattern, "Expected %s to match '%s'", reference, pattern == null ? "null" : pattern.pattern());
    }

    /**
     * Performs a runtime check if the reference is an instance of the provided class
     *
     * @param class_               the class to use
     * @param reference            reference to check
     * @param errorMessageTemplate the exception message template to use if the check fails; will
     *                             be converted to a string using {@link String#valueOf(Object)}
     * @param errorMessageArgs     the <tt>errorMessageTemplate</tt> arguments
     * @param <T>                  the reference type
     * @return the original reference
     * @throws IllegalArgumentException if the {@code reference} is not an instance of provided class {@code class_}
     * @throws NullPointerException     if the {@code reference} is null; also when the check fails and either
     *                                  {@code errorMessageTemplate} or {@code errorMessageArgs} is null (don't let this happen)
     * @see Class#isInstance(Object)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference,
                                        @Nullable String errorMessageTemplate,
                                        @Nullable Object... errorMessageArgs) {
        checkArgument(class_ != null, "Expected non-null class_");
        checkArgument(reference != null, "Expected non-null reference");
        checkArgument(class_.isInstance(reference), errorMessageTemplate, errorMessageArgs);
        //noinspection unchecked
        return (T) reference;
    }

    /**
     * Performs a runtime check if the reference is an instance of the provided class
     *
     * @param class_       the class to use
     * @param reference    reference to check
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @param <T>          the reference type
     * @see Checks#checkIsInstance(Class, Object, String, Object...)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference,
                                        @Nullable String errorMessage) {
        return checkIsInstance(class_, reference, errorMessage, EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Performs a runtime check if the reference is an instance of the provided class
     *
     * @param class_    the class to use
     * @param reference reference to check
     * @param <T>       the reference type
     * @see Checks#checkIsInstance(Class, Object, String, Object...)
     */
    @Beta
    public static <T> T checkIsInstance(Class<T> class_, Object reference) {
        return checkIsInstance(class_, reference, "Expected reference to be instance of %s, got %s",
                class_ == null ? "null" : class_.getName(), reference == null ? "null" : reference.getClass().getName());
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException     if the {@code uri} is null
     * @see java.net.URI
     */
    public static String checkUri(String uri,
                                  @Nullable String errorMessageTemplate,
                                  @Nullable Object... errorMessageArgs) {
        checkArgument(uri != null, "Expected non-null uri");
        checkArgument(uri.length() > 0 && uri.length() < 2000,
                "Expected a email in range 1 to 2000 characters, got %s", uri.length());
        return check(uri, isValidURI(), errorMessageTemplate, errorMessageArgs);
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException     if the {@code uri} is null
     * @see Checks#checkUri(String, String, Object...)
     */
    public static String checkUri(String uri, @Nullable Object errorMessage) {
        return checkUri(uri, String.valueOf(errorMessage), EMPTY_ERROR_MESSAGE_ARGS);
    }

    /**
     * Performs URI check against RFC 2396 specification
     *
     * @param uri the URI to check
     * @return the checked uri
     * @throws IllegalArgumentException if the {@code uri} is invalid
     * @throws NullPointerException     if the {@code uri} is null
     * @see Checks#checkUri(String, String, Object...)
     */
    public static String checkUri(String uri) {
        return checkUri(uri, "Expected a valid URI, got %s", uri);
    }

    /**
     * Performs email address check against RFC 822 specification
     * <p>
     * TODO:  RFC3696, RFC 5322 and RFC 5321
     *
     * @param email an Email to check
     * @return checked Email
     * @throws IllegalArgumentException if the {@code email} is invalid
     * @see <a href="http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690">RFC3696 Errata</a>
     */
    public static String checkEmail(String email) {
        checkArgument(email != null, "Expected non-null email");
        checkArgument(email.length() > 0 && email.length() < 255,
                "Expected a email in range 1 to 254 characters, got %s", email.length());

        String[] split = email.split("@", 2);
        String localPart = split[0];
        String domainPart = split[1];
        checkArgument(localPart.length() > 0 && localPart.length() < 64,
                "Expected a email local part in range 1 to 63 characters, got %s", localPart.length());
        checkArgument(domainPart.length() > 0 && domainPart.length() < 256,
                "Expected a email domain part in range 1 to 255 characters, got %s", domainPart.length());
        return check(email, isValidEmail(), email);
    }

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

        checkArgument(hostname != null, "%sExpected non-null hostname", message);
        checkArgument(hostname.length() > 0 && hostname.length() < 256,
                "%sExpected a hostname in range 1 to 255 characters, got %s", message, hostname.length());

        Iterable<String> labels = on('.').split(hostname);
        for (String label : labels) {
            checkArgument(label.length() > 0 && label.length() < 64,
                    "%sExpected a hostname label in range 1 to 63 characters, got %s", message, label.length());
        }

        checkMatches(hostname, compile(REGEXP_HOSTNAME),
                "%sExpected a hostname to match expression %s, got: %s", message, REGEXP_HOSTNAME, hostname);
        return hostname;
    }

    /**
     * @see Checks#checkHostname(String, String, Object...)
     */
    @Beta
    public static String checkHostname(String hostname) {
        return checkHostname(hostname, "", "");
    }

    /**
     * @see Checks#checkHostname(String, String, Object...)
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

    private Checks() {
        throw new UnsupportedOperationException("Private constructor");
    }
}
