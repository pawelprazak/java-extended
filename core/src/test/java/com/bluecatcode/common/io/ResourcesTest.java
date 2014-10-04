package com.bluecatcode.common.io;

import org.hamcrest.Matcher;
import org.junit.Test;

import static com.bluecatcode.common.io.Resources.getResourceAsString;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResourcesTest {

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
    public void shouldGetResourceAsStringFromCurrentClassLoader() throws Exception {
        // given
        String propertiesResource = "samples/test-resource.properties";
        String classResource = "com/bluecatcode/common/io/Resources.class";
        String externalClassResource = "javax/mail/Address.class";

        // when
        String propertiesString = getResourceAsString(propertiesResource);
        String classString = getResourceAsString(classResource);
        String externalClassString = getResourceAsString(externalClassResource);

        // then
        assertThat(propertiesString, isNotEmptyString());
        assertThat(propertiesString, startsWith("testKey"));
        assertThat(classString, isNotEmptyString());
        assertThat(classString, containsString("com/bluecatcode/common/io/Resources"));
        assertThat(externalClassString, isNotEmptyString());
        assertThat(externalClassString, containsString("javax/mail/Address"));
    }

    private Matcher<String> isNotEmptyString() {
        return is(not(isEmptyOrNullString()));
    }
}