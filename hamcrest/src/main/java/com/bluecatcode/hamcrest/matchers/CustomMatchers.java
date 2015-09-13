package com.bluecatcode.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class CustomMatchers {

    public static String fixedDescription(Matcher matcher) {
        return fixedDescription("", matcher);
    }

    public static String fixedDescription(String description, Matcher matcher) {
        if (description == null) {
            throw new IllegalArgumentException("Expected non-null description");
        }
        if (matcher == null) {
            throw new IllegalArgumentException("Expected non-null matcher");
        }

        StringDescription stringDescription = new StringDescription();
        matcher.describeTo(stringDescription);
        return description + stringDescription.toString();
    }

    public static Description mismatchDescriptionFrom(Object actual, Matcher matcher) {
        return mismatchDescriptionFrom(actual, matcher, "");
    }

    public static Description mismatchDescriptionFrom(Object actual, Matcher matcher, String reason) {
        Description description = new StringDescription();
        description.appendText(reason)
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ");
        matcher.describeMismatch(actual, description);
        return description;
    }

    private CustomMatchers() {
    	throw new UnsupportedOperationException("Private constructor");
    }
}