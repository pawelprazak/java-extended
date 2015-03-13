package com.bluecatcode.common.collections;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static pl.com.britenet.common.collections.Collections3Test.DictionaryData.dictionaryData;
import static pl.com.britenet.common.collections.Collections3Test.MapData.mapData;
import static pl.com.britenet.common.collections.Collections3Test.ZipData.zipData;

@RunWith(Theories.class)
public class Collections3Test {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @DataPoints
    public static final ZipData[] zipSamples = new ZipData[]{
            zipData(ImmutableList.<String>of(), ImmutableList.<String>of(), ImmutableMap.<String, String>of()),
            zipData(ImmutableList.of("lol"), ImmutableList.of("cat"), ImmutableMap.of("lol", "cat")),
    };

    @Theory
    public void shouldZip(ZipData data) throws Exception {
        // given
        List<String> list1 = data.list1();
        List<String> list2 = data.list2();

        // when
        Map<String, String> result = Collections3.zip(list1, list2);

        // then
        assertThat(result, is(data.map()));
    }

    @Test
    public void shouldThrowWhenUnequalListsInZip() throws Exception {
        // given
        List<String> list1 = ImmutableList.of("lol", "lol2");
        List<String> list2 = ImmutableList.of("cat");

        // expect
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(startsWith("Expected equal size"));

        // when
        Collections3.zip(list1, list2);
    }

    @DataPoints
    public static final DictionaryData[] dictionarySamples = new DictionaryData[]{
            dictionaryData(new Hashtable<>(ImmutableMap.<String, String>of()), ImmutableMap.<String, String>of()),
            dictionaryData(new Hashtable<>(ImmutableMap.of("lol", "cat")), ImmutableMap.of("lol", "cat")),
            dictionaryData(new Supplier<Dictionary<String, String>>() {
                @Override
                public Dictionary<String, String> get() {
                    Hashtable<String, String> dict = new Hashtable<>();
                    dict.put("lol", "cat");
                    dict.put("lol", "cat");
                    return dict;
                }
            }.get(), ImmutableMap.of("lol", "cat"))
    };

    @Theory
    public void shouldConvertFromDictionary(DictionaryData data) throws Exception {
        // given
        Dictionary<String, String> dictionary = data.dictionary();

        // when
        Map<String, String> map = Collections3.fromDictionary(dictionary);

        // then
        assertThat(map, is(data.map()));
    }

    @DataPoints
    public static final MapData[] samples = new MapData[]{
            mapData(null, null, IllegalArgumentException.class),
            mapData(null, emptyHashMap(), IllegalArgumentException.class),
            mapData(emptyHashMap(), null, IllegalArgumentException.class),
            mapData(emptyHashMap(), emptyHashMap(), isAnEmptyMap()),
            mapData(emptyHashMap(), hashMapWith(null, null), isAnEmptyMap()),
            mapData(hashMapWith("1", null), emptyHashMap(), hasEntry("1", "")),
            mapData(hashMapWith("1", null), ImmutableMap.of("2", "2"), allOf(
                    hasEntry("1", ""), hasEntry("2", "2"))
            ),
            mapData(ImmutableMap.of("2", "2", "3", "3"), ImmutableMap.of("2", "2", "3", "3"), allOf(
                            hasEntry("2", "2"), hasEntry("3", "3"))
            ),
    };

    private static Matcher<Map<?, ?>> isAnEmptyMap() {
        return allOf(notNullValue(), not(hasEntry(notNullValue(), notNullValue())));
    }

    @Theory
    public void shouldMergeMaps(MapData data) {
        // given
        Map<String, String> first = data.first();
        Map<String, String> second = data.second();
        Optional<Matcher<? super Map<String, String>>> matcher = data.matcher();
        Optional<Class<? extends Throwable>> expectedException = data.exception();

        // expect
        if (expectedException.isPresent()) {
            exception.expect(expectedException.get());
        }

        // when
        Map<String, String> map = Collections3.mergeMaps(first, second, "");

        // then
        if (matcher.isPresent()) {
            assertThat(map, matcher.get());
        }
    }

    @AutoValue
    public static abstract class ZipData {

        public static ZipData zipData(List<String> list1, List<String> list2, Map<String, String> map) {
            return new AutoValue_Collections3Test_ZipData(list1, list2, map);
        }

        public abstract List<String> list1();

        public abstract List<String> list2();

        public abstract Map<String, String> map();
    }

    @AutoValue
    public static abstract class DictionaryData {

        public static DictionaryData dictionaryData(Dictionary<String, String> dictionary, Map<String, String> map) {
            return new AutoValue_Collections3Test_DictionaryData(dictionary, map);
        }

        public abstract Dictionary<String, String> dictionary();

        public abstract Map<String, String> map();
    }

    @AutoValue
    public static abstract class MapData {

        public static MapData mapData(Map<String, String> first, Map<String, String> second,
                                      Matcher<? super Map<String,String>> matcher) {
            return new AutoValue_Collections3Test_MapData(first, second,
                    Optional.<Matcher<? super Map<String,String>>>of(matcher),
                    Optional.<Class<? extends Throwable>>absent()
            );
        }

        public static MapData mapData(Map<String, String> first, Map<String, String> second,
                                      Class<? extends Throwable> exception) {
            return new AutoValue_Collections3Test_MapData(first, second,
                    Optional.<Matcher<? super Map<String,String>>>absent(),
                    Optional.<Class<? extends Throwable>>of(exception));
        }

        public abstract @Nullable Map<String, String> first();

        public abstract @Nullable Map<String, String> second();
        public abstract Optional<Matcher<? super Map<String,String>>> matcher();
        public abstract Optional<Class<? extends Throwable>> exception();
    }

    private static HashMap<String, String> emptyHashMap() {
        return new HashMap<String, String>();
    }

    private static HashMap<String, String> hashMapWith(String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        return map;
    }

}