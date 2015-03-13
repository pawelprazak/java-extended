package pl.com.britenet.common.base;

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

public class Strings {

    private static final String ANSI_CODES_EXPRESSION = "\u001B\\[[;\\d]*[ -/]*[@-~]";

    @Beta
    public static String stripAnsiCodes(String line) {
        return line.replaceAll(ANSI_CODES_EXPRESSION, "");
    }

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
    public static String asString(Map<?, ?> map) {
        return mapAsString(map, ",");
    }

    @Beta
    public static String asString(Map<?,?> map, String seprator) {
        return on(seprator).withKeyValueSeparator(": ").join(map);
    }

    @Beta
    public static List<String> asStringList(String string) {
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
