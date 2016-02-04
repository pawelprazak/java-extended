package com.bluecatcode.common.collections;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.getOnlyElement;

/**
 * Collection utility functions: {@link #zip}, {@link #fromDictionary}, {@link #mergeMaps}, {@link #getOnlyEntry}
 */
public class Collections3 {

    /**
     * Combine two lists into a map. Lists must have the same size.
     *
     * @param keys   the key list
     * @param values the value list
     * @param <K>    the key type
     * @param <V>    the value type
     * @return the resulting map
     * @throws IllegalArgumentException if lists size differ
     * @throws NullPointerException     if any of the lists is null
     */
    @Beta
    public static <K, V> Map<K, V> zip(List<K> keys, List<V> values) {
        checkArgument(keys != null, "Expected non-null keys");
        checkArgument(values != null, "Expected non-null values");
        checkArgument(keys.size() == values.size(), "Expected equal size of lists, got %s and %s", keys.size(), values.size());

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (int i = 0; i < keys.size(); i++) {
            builder.put(keys.get(i), values.get(i));
        }

        return builder.build();
    }

    /**
     * Convert a {@link Dictionary} to a {@link Map}
     *
     * @param dictionary the dictionary to convert
     * @param <K>        the key type
     * @param <V>        the value type
     * @return the resulting map
     */
    @Beta
    public static <K, V> Map<K, V> fromDictionary(Dictionary<K, V> dictionary) {
        checkArgument(dictionary != null, "Expected non-null dictionary");

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (Enumeration<K> keys = dictionary.keys(); keys.hasMoreElements(); ) {
            K key = keys.nextElement();
            V value = dictionary.get(key);
            builder.put(key, value);
        }

        return builder.build();
    }

    /**
     * Convert a {@link Dictionary} to a {@link Map} using a {@link Function}
     *
     * @param dictionary  the dictionary to convert
     * @param transformer the dictionary value transformer
     * @param <K>         the key type
     * @param <V>         the value type
     * @return the resulting map
     */
    @Beta
    @SuppressWarnings("ConstantConditions")
    public static <K, V> Map<K, V> fromDictionary(Dictionary<K, ?> dictionary, Function<Object, V> transformer) {
        checkArgument(dictionary != null, "Expected non-null dictionary");
        checkArgument(transformer != null, "Expected non-null transformer");

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
     * Gets the only element from a given map or throws an exception
     *
     * @param map the map to get only entry from
     * @param <K> the key type
     * @param <V> the value type
     * @return the single entry contained in the map
     * @throws NoSuchElementException   if the map is empty
     * @throws IllegalArgumentException if the map contains multiple entries
     * @see com.google.common.collect.Iterables#getOnlyElement
     */
    @Beta
    public static <K, V> Map.Entry<K, V> getOnlyEntry(Map<K, V> map) {
        checkArgument(map != null, "Expected non-null map");
        return getOnlyElement(map.entrySet());
    }

    /**
     * Mergers two maps handling null keys, null values and duplicates
     *
     * @param first        the first map to merge
     * @param second       the second map to merge
     * @param defaultValue the value to be used in case of null value
     * @param <K> the key type
     * @param <V> the value type
     * @return a single map containing entries from the given maps
     * @throws IllegalArgumentException if any of the arguments is null
     */
    @Beta
    public static <K, V> Map<K, V> mergeMaps(Map<K, V> first, Map<K, V> second, V defaultValue) {
        checkArgument(first != null, "Expected non-null first map");
        checkArgument(second != null, "Expected non-null second map");
        checkArgument(defaultValue != null, "Expected non-null default value");

        Map<K, V> map = new HashMap<K, V>();
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
