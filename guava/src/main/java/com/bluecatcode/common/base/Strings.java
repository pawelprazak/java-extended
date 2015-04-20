package com.bluecatcode.common.base;

import com.google.common.annotations.Beta;
import com.google.common.io.CharStreams;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@Beta
public class Strings {

    /**
     * Returns the number of times the token appears in the target.
     * @param token Token value to be counted.
     * @param target Target value to count tokens in.
     * @return the number of tokens.
     */
    @SuppressWarnings("ConstantConditions")
    @Beta
    public static int countToken(@Nullable String target, String token) {
        checkArgument(token != null && !token.isEmpty(), "Expected non-empty token, got: '%s'", token);

        if (isNullOrEmpty(target)) {
            return 0;
        }

        int count = 0;
        int tokenIndex = 0;
        while ((tokenIndex = target.indexOf(token, tokenIndex)) != -1) {
            count++;
            tokenIndex += token.length();
        }
        return count;
    }

    @Beta
    public static String limitCharacters(String string, int max) {
        checkArgument(string != null, "Expected non-null string");
        checkArgument(max >= 0, "Expected non-negative max");
        String ellipsis = "...";
        int ellipsisLength = ellipsis.length();
        int length = string.length();
        if (length <= max) {
            return string;
        } else if (max <= ellipsisLength) {
            return string.substring(0, max);
        } else {
            return string.substring(0, max - ellipsisLength) + ellipsis;
        }
    }

    @Beta
    public static String capitalize(String string) {
        checkArgument(string != null, "Expected non-null string");
        if (string.isEmpty()) {
            return string;
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    @Beta
    public static String asString(Map<?, ?> map) {
        return asString(map, ", ");
    }

    @Beta
    public static String asString(Map<?,?> map, String separator) {
        return on(separator).withKeyValueSeparator(": ").join(map);
    }

    @Beta
    public static List<String> asStringList(String string) {
        checkArgument(string != null, "Expected non-null string");
        try {
            //noinspection ConstantConditions
            return CharStreams.readLines(new StringReader(string));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);

        }
    }

    private Strings() {
        throw new UnsupportedOperationException();
    }

}
