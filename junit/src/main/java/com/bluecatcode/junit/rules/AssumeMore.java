package com.bluecatcode.junit.rules;

import org.apache.commons.lang3.SystemUtils;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;

public class AssumeMore {

    public static void assumeIsUnix() {
        assumeThat(format("Expected UNIX, got SystemUtils.IS_OS_UNIX: %s", SystemUtils.IS_OS_UNIX), SystemUtils.IS_OS_UNIX, is(true));
    }

    public static void assumeIsWindows() {
        assumeThat(format("Expected Windows, got SystemUtils.IS_OS_WINDOWS: %s", SystemUtils.IS_OS_WINDOWS), SystemUtils.IS_OS_WINDOWS, is(true));
    }
}
