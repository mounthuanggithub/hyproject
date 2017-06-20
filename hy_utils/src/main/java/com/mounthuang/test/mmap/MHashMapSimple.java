package com.mounthuang.test.mmap;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Author: mounthuang
 * Data: 2017/6/20.
 */
public class MHashMapSimple<K, V> implements MMap<K, V> {
    static class Entry<K, V> implements MMap.Entry<K, V> {
        final K key;
        V value;
        final int hash;
        MHashMapSimple.Entry<K, V> next;

        Entry(int h, K k, V v, MHashMapSimple.Entry<K, V> n) {
            key = k;
            value = v;
            next = n;
            hash = h;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof MMap.Entry) {
                MMap.Entry<?, ?> e = (MMap.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * length为2的n次方时，h&(length - 1)就相当于对length取模
     *
     * @param h
     * @param length
     * @return
     */
    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    transient Entry<K, V>[] table;
    transient Set<Entry<K, V>> entrySet;
    transient int size;
    transient int modCount;
    transient volatile Set<K> keySet;
    transient volatile Collection<V> values;
    // map中元素个数的阈值
    int threshold;
    final float loadFactor;

    public MHashMapSimple(int initialCapacity, float loadFactory) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity");
        }

        if (initialCapacity > 1 << 30) {
            initialCapacity = 1 << 30;
        }

        if (loadFactory <= 0 || Float.isNaN(loadFactory)) {
            throw new IllegalArgumentException("Illegal initial loadFactory");
        }

        int capacity = 1;
        // 大于元素个数的最小的2的n次方
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactory;
        this.threshold = (int) (capacity * loadFactory);
        table = new Entry[capacity];
    }

    public MHashMapSimple(int initialCapacity) {
        this(initialCapacity, 0.75F);
    }

    public MHashMapSimple() {
        this(1 << 4, 0.75F);
    }


    public Collection<V> values() {
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    public boolean containsValue(Object value) {
        Entry[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (value == null && e.value == null) {
                    return true;
                } else if (value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public V get(Object key) {
        if (key == null) {
            return table[0].value;
        }
        int hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }
        int hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }

        }
        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        modCount++;
        //存储在index=0位置
        addEntry(0, null, value, 0);
        return null;
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> e = table[bucketIndex];
        // 将新创建的 Entry 放入 bucketIndex 索引处，并让新的 Entry 指向原来的 Entry
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        if (size++ >= threshold) { // 如果大于临界值就扩容(临界值=数组大小*loadFactor)
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1 << 30) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Entry[] newTable) {
        int newCapacity = newTable.length;
        for (Entry<K, V> e : table) {
            while (null != e) {
                Entry<K, V> next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    public V remove(K key) {
        if (key == null) {
            if (table[0].next == null) {
                return null;
            } else {
                V value = table[0].next.value;
                table[0].next = null;
                return value;
            }
        }
        return null;
    }

    public void putAll(MMap<? extends K, ? extends V> m) {

    }

    public void clear() {

    }

    public Set<K> keySet() {
        return null;
    }

    public Set<MMap.Entry<K, V>> entrySet() {
        return null;
    }

    private Entry<K, V> getEntry(Object key) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        for (Entry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                return e;
            }
        }
        return null;
    }
}
