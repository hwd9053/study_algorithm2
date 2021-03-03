package com.mj;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class SkipList<K, V> {
    private static final int MAX_LEVEL = 32;
    private int size;
    private Comparator<K> comparator;
    // 有效层数
    private int level;
    // 不存放任何内容的首节点
    private Node<K, V> first;

    public SkipList(Comparator<K> comparator) {
        this.comparator = comparator;
        first = new Node<>();
        first.nexts = new Node[MAX_LEVEL];
    }

    public SkipList() {
        this(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        keyCheck(key);
        return null;
    }

    public V get(K key) {
        keyCheck(key);
        Node<K, V> node = first;
        for (int i = level; i <= 0 ; i--) {
            while (compare(node.nexts[i].key, key) < 0) {
                node = node.nexts[i];
            }
            if (compare(node.nexts[i].key, key) == 0) return node.nexts[i].value;
        }
        return null;
    }

    public V remove(K key) {
        keyCheck(key);
        return null;
    }

    private int compare(K k1, K k2) {
        if (comparator != null) return comparator.compare(k1, k2);
        return ((Comparable<K>)k1).compareTo(k2);
    }

    private void keyCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V>[] nexts;
    }
}
