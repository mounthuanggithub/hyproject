package com.mounthuang.test.mmap;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Author: mounthuang
 * Data: 2017/6/19.
 */
public interface MMap<K, V> {

    Collection<V> values();

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    V put(K key, V value);

    V remove(K key);

    void putAll(MMap<? extends K, ? extends V> m);

    void clear();

    Set<K> keySet();

    Set<MMap.Entry<K, V>> entrySet();

    boolean equals(Object o);

    int hashCode();

    // Default方法是指，在接口内部包含了一些默认的方法实现（也就是接口中可以包含方法体，这打破了1.8之前版本对接口的语法限制）
//    default void forEach(BiConsumer<? extends K, ? extends V> action) {
//        Objects.requireNonNull(action);
//        for (MMap.Entry<K, V> entry : entrySet()) {
//            K k;
//            V v;
//            try {
//                k = entry.getKey();
//                v = entry.getValue();
//            } catch (IllegalStateException ise) {
//                // this usually means the entry is no longer in the map.
//                throw new ConcurrentModificationException(ise);
//            }
//            action.accept(k, v);
//        }
//    }

    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }

}
