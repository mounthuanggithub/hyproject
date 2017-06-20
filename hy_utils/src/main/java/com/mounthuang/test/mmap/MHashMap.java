package com.mounthuang.test.mmap;


import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Author: mounthuang
 * Data: 2017/6/19.
 */
public class MHashMap<K, V> implements MMap<K, V> {

    static class Node<K, V> implements MMap.Entry<K, V> {
        final K key;
        final int hash;
        V value;
        Node<K, V> next;

        Node(int h, K k, V v, Node<K, V> n) {
            key = k;
            value = v;
            next = n;
            hash = h;
        }

        public final K getKey() {
            return null;
        }

        public final V getValue() {
            return null;
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

    //------------------------静态utils------------------//
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    /**
     * 默认初始容量，2的n次方加快hash计算速度
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16

    /**
     * 最大容量，2的30次方
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认负载因子
     */
    static final float DAFAULT_LOAD_FACTOR = 0.75F;

    /**
     * 当桶(bucket)上的链表数大于这个值时会转成红黑树
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 当桶(bucket)上的链表数小于这个值时树转链表
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 树的最小的容量，至少是 4 x TREEIFY_THRESHOLD = 32 以避免冲突
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * 存储元素的实体数组
     * 用transient关键字标记的成员变量不参与序列化过程
     */
    transient MHashMap.Node<K, V>[] table;

    /**
     *
     */
    transient Set<MMap.Entry<K, V>> entrySet;

    /**
     * 存放元素的个数
     */
    transient int size;

    /**
     * 被修改的次数
     */
    transient int modCount;

    /**
     * 临界值
     */
    int threshold;

    /**
     * 加载因子，表示Hsah表中元素的填满的程度
     * 加载因子越大，填满的元素越多，好处是空间利用率高了，但冲突的机会增加，链表长度会越来越长,查找效率降低
     * 加载因子越小，填满的元素越少，好处是冲突的机会减小了，但空间浪费多了，表中的数据将过于稀疏
     */
    final float loadFactor;

    public MHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factory: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public MHashMap(int initialCapacity) {
        this(initialCapacity, DAFAULT_LOAD_FACTOR);
    }

    public MHashMap() {
        this.loadFactor = DAFAULT_LOAD_FACTOR;
    }

    public MHashMap(MMap<? extends K, ? extends V> m) {
        this.loadFactor = DAFAULT_LOAD_FACTOR;
        //putMapEntries(m, false);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public V get(Object value) {
        return null;
    }

    public V put(K key, V value) {
        return null;
    }

    public V putVal(int hash, K key, V value, boolean onlyIdAbsent, boolean evict) {
//        Node<K, V>[] tab;
//        Node<K, V> p;
//        int n, j;
//        if ((tab = table) == null || (n = tab.length) == 0) {
//            n = (tab = resize()).length;
//        }
        return null;
    }

    public V remove(K key) {
        return null;
    }

    public void putAll(MMap<? extends K, ? extends V> m) {

    }

    public void clear() {

    }

    public Set<K> keySet() {
        return null;
    }

    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    public Collection<V> values() {
        return null;
    }


}
