package com.envyful.api.registry;

import com.envyful.api.registry.impl.MapBasedRegistry;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * An interface representing a registry of keys to values
 *
 * @param <A> The key type
 * @param <B> The value type
 */
public interface Registry<A, B> {

    /**
     *
     * Gets the value for the given key
     *
     * @param key The key
     * @return The value, or null if not found
     */
    @Nullable
    B get(A key);

    /**
     *
     * Gets the value for the given key
     *
     * @param key The key
     * @return The value, or empty if not found
     */
    default Optional<B> getValue(A key) {
        return Optional.ofNullable(this.get(key));
    }

    /**
     *
     * Gets the key for the given value
     *
     * @param value The value
     * @return The key, or null if not found
     */
    A getKey(B value);

    /**
     *
     * Gets the key for the given value
     *
     * @param value The value
     * @return The key, or empty if not found
     */
    default Optional<A> getKeyForValue(B value) {
        return Optional.ofNullable(this.getKey(value));
    }

    /**
     *
     * Registers the given key to the given value
     *
     * @param key The key
     * @param value The value
     */
    void register(A key, B value);

    /**
     *
     * Unregisters the given key
     *
     * @param key The key
     */
    void unregister(A key);

    /**
     *
     * Clears all the values
     *
     */
    void clear();

    /**
     *
     * Gets all the values
     *
     * @return The values
     */
    List<B> values();

    /**
     *
     * Gets all the keys
     *
     * @return The keys
     */
    Set<A> keys();

    /**
     *
     * Creates a new registry
     *
     * @param <A> The key type
     * @param <B> The value type
     * @return The new registry
     */
    static <A, B> Registry<A, B> create() {
        return new MapBasedRegistry<>(HashMap::new);
    }

    /**
     *
     * Creates a new registry
     *
     * @param <A> The key type
     * @param <B> The value type
     * @return The new registry
     */
    static <A, B> Registry<A, B> concurrent() {
        return new MapBasedRegistry<>(Maps::newConcurrentMap);
    }
}
