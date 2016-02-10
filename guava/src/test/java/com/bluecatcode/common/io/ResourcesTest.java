package com.bluecatcode.common.io;

import com.google.common.io.LineProcessor;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.bluecatcode.common.io.Resources.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResourcesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldGetResourceAsStringFromContext() throws Exception {
        // given
        String classResource = "../base/Predicates.class";

        // when
        String classString = getResourceAsString(Resources.class, classResource);

        // then
        assertThat(classString, isNotEmptyString());
        assertThat(classString, containsString("com/bluecatcode/common/base/Predicates"));
    }

    @Test
    public void shouldGetResourceWithLineProcessorFromContext() throws Exception {
        // given
        String classResource = "../base/Predicates.class";

        // when
        String classString = getResourceWith(Resources.class, classResource, new LineProcessor<String>() {
            StringBuilder builder = new StringBuilder();

            @Override
            public boolean processLine(String line) throws IOException {
                builder.append(line).append("\n");
                return true;
            }

            @Override
            public String getResult() {
                return builder.toString();
            }
        });

        // then
        assertThat(classString, isNotEmptyString());
        assertThat(classString, containsString("com/bluecatcode/common/base/Predicates"));
    }

    @Test
    public void shouldWrapExceptionInGetResourceWithLineProcessorFromContext() throws Exception {
        // given
        String classResource = "../base/Predicates.class";

        // expect
        exception.expect(IllegalStateException.class);

        // when
        getResourceWith(Resources.class, classResource, new LineProcessor<String>() {

            @Override
            public boolean processLine(String line) throws IOException {
                throw new IOException("test");
            }

            @Override
            public String getResult() {
                return null;
            }
        });
    }

    @Test
    public void shouldGetResourceAsFilePathFromContext() throws Exception {
        // given
        String classResource = "../base/Predicates.class";

        // when
        String path = getResourceAsFilePath(Resources.class, classResource);

        // then
        assertThat(path, isNotEmptyString());
        assertThat(path, endsWith("com/bluecatcode/common/base/Predicates.class"));
    }

    @Test
    public void shouldGetResourceAsFilePathFromCurrentClassLoader() throws Exception {
        // given
        String classResource = "com/bluecatcode/common/base/Predicates.class";

        // when
        String path = getResourceAsFilePath(getContextClassLoader(), classResource);

        // then
        assertThat(path, isNotEmptyString());
        assertThat(path, endsWith("com/bluecatcode/common/base/Predicates.class"));
    }

    @Test
    public void shouldGetResourceAsStreamFromContext() throws Exception {
        // given
        String classResource = "../base/Predicates.class";

        // when
        InputStream stream = getResourceAsStream(Resources.class, classResource);

        // then
        assertThat(stream, is(notNullValue()));
        assertThat(stream.available(), greaterThan(0));

        Closeables.closeQuietly(stream);
    }

    @Test
    public void shouldGetResourceAsStringFromCurrentClassLoader() throws Exception {
        // given
        String propertiesResource = "samples/test-resource.properties";
        String classResource = "com/bluecatcode/common/io/Resources.class";
        String externalClassResource = "javax/mail/Address.class";

        // when
        String propertiesString = getResourceAsString(getContextClassLoader(), propertiesResource);
        String classString = getResourceAsString(getContextClassLoader(), classResource);
        String externalClassString = getResourceAsString(getContextClassLoader(), externalClassResource);

        // then
        assertThat(propertiesString, isNotEmptyString());
        assertThat(propertiesString, startsWith("testKey"));
        assertThat(classString, isNotEmptyString());
        assertThat(classString, containsString("com/bluecatcode/common/io/Resources"));
        assertThat(externalClassString, isNotEmptyString());
        assertThat(externalClassString, containsString("javax/mail/Address"));
    }

    @Test
    public void shouldGetResourceAsPropertiesFromContext() throws Exception {
        // given
        String propertiesResource = "/samples/test-resource.properties";

        // when
        Properties properties = getResourceAsProperties(Resources.class, propertiesResource);

        // then
        assertThat(properties, is(notNullValue()));
        assertThat(properties.size(), is(2));
        assertThat(properties.stringPropertyNames(), containsInAnyOrder("testKey", "testKey2"));
    }

    private Matcher<String> isNotEmptyString() {
        return is(not(isEmptyOrNullString()));
    }
}