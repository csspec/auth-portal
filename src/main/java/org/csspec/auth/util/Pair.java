package org.csspec.auth.util;

/**
 * small C++ std::pair based util class
 * @param <T> first type
 * @param <V> second type
 */
public class Pair<T, V> {
    public final T first;
    public final V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }
}
