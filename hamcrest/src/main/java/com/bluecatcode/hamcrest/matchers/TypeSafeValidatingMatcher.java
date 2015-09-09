package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;

/**
 * Convenience class for writing one off matchers with validation.
 * <p>
 * You should provide a description and override the validation method.
 * <p>
 * Usage:<br>
 * <pre>
 *  public class IsValidEmail extends TypeSafeValidatingMatcher&lt;String&gt; {
 *      private static final IsValidEmail NOT_EMPTY_INSTANCE = new IsValidEmail();
 *
 *      public IsValidEmail() {
 *          super("a valid e-mail (RFC822)");
 *      }
 *
 *      {@literal@}Override
 *      protected boolean isValid(String item, Description errorDescription) {
 *          try {
 *              new InternetAddress(item).validate();
 *          } catch (AddressException ex) {
 *              errorDescription.appendText(ex.getMessage());
 *              return false;
 *          }
 *          return true;
 *      }
 *
 *      {@literal@}Factory
 *      public static Matcher&lt;? super String&gt; isValidEmail() {
 *          return NOT_EMPTY_INSTANCE;
 *      }
 *  }
 *
 * </pre>
 *
 * @param <T> The type of object being matched
 */
public abstract class TypeSafeValidatingMatcher<T> extends CustomTypeSafeMatcher<T> {

    private Description errorDescription = new StringDescription();

    public TypeSafeValidatingMatcher(String description) {
        super(description);
    }

    protected abstract boolean isValid(T item, Description errorDescription);

    @Override
    protected boolean matchesSafely(T item) {
        return item != null && isValid(item, errorDescription);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendValue(item)
                .appendText(" is not valid, cause: ")
                .appendText(errorDescription.toString());
    }
}
