package com.bluecatcode.common.collections;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getOnlyElement;

public class Collections3 {

    @Beta
    public static <K, V> Map<K, V> zip(List<K> keys, List<V> values) {
        checkArgument(keys.size() == values.size(), "Expected equal size of lists, got %s and %s", keys.size(), values.size());

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (int i = 0; i < keys.size(); i++) {
            builder.put(keys.get(i), values.get(i));
        }

        return builder.build();
    }

    @Beta
    public static <K, V> Map<K, V> fromDictionary(Dictionary<K, V> dictionary) {
        checkNotNull(dictionary);

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (Enumeration<K> keys = dictionary.keys(); keys.hasMoreElements(); ) {
            K key = keys.nextElement();
            V value = dictionary.get(key);
            builder.put(key, value);
        }

        return builder.build();
    }

    @Beta
    @SuppressWarnings("ConstantConditions")
    public static <K, V> Map<K, V> fromDictionary(Dictionary<K, ?> dictionary, Function<Object, V> transformer) {
        checkNotNull(dictionary);

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (Enumeration<K> keys = dictionary.keys(); keys.hasMoreElements(); ) {
            K key = keys.nextElement();
            V value = transformer.apply(dictionary.get(key));
            checkArgument(value != null, "Provided transformer returned null");
            builder.put(key, value);
        }

        return builder.build();
    }

    /**
     * @return the single entry contained in the map
     * @throws java.util.NoSuchElementException if the map is empty
     * @throws IllegalArgumentException if the map contains multiple entries
     * @see com.google.common.collect.Iterables#getOnlyElement
     */
    @Beta
    public static <K, V> Map.Entry<K, V> getOnlyEntry(Map<K, V> map) {
        return getOnlyElement(map.entrySet());
    }

    /**
     * Mergers two maps handling null keys, null values and duplicates
     * @param first the first map to merge
     * @param second the second map to merge
     * @param defaultValue the value to be used in case of null value
     * @return a single map containing entries from the given maps
     * @throws IllegalArgumentException if any of the arguments is null
     */
    @Beta
    public static <K, V> Map<K, V> mergeMaps(Map<K, V> first, Map<K, V> second, V defaultValue) {
        checkArgument(first != null, "Expected non-null first map");
        checkArgument(second != null, "Expected non-null second map");
        checkArgument(defaultValue != null, "Expected non-null default value");

        Map<K, V> map = new HashMap<>();
        //noinspection ConstantConditions
        for (Map.Entry<K, V> entry : first.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue() == null ? defaultValue : entry.getValue());
        }
        //noinspection ConstantConditions
        for (Map.Entry<K, V> entry : second.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue() == null ? defaultValue : entry.getValue());
        }
        return map;
    }

    private Collections3() {
        throw new UnsupportedOperationException();
    }

}
