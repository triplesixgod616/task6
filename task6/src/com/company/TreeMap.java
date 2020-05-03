package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TreeMap<T extends Comparable<? super T>, V> {
    private RedBlackTree<T, V> tree;
    private int size;

    public TreeMap() {
        tree = new RedBlackTree<>();
        size = 0;
    }

    public void clear() {
        tree.clear();
        size = 0;
    }

    public Set<RedBlackTree.Node<T, V>> nodeSet() {
        Set<RedBlackTree.Node<T, V>> set = new HashSet<>();

        tree.preOrderVisit(tree.getRoot(), n -> set.add(n));

        return set;
    }

    public ArrayList<T> keySet() {
        Set<RedBlackTree.Node<T, V>> nodes = nodeSet();
        ArrayList<T> set = new ArrayList<>();

        for(RedBlackTree.Node<T, V> n : nodes) {
            set.add(n.key);
        }

        return set;
    }

    public ArrayList<V> values() {
        Set<RedBlackTree.Node<T, V>> nodes = nodeSet();
        ArrayList<V> list = new ArrayList<>();

        for(RedBlackTree.Node<T, V> n : nodes) {
            list.add(n.value);
        }

        return list;
    }

    public V put(T key, V value) {
        V oldValue = tree.put(key, value);

        size++;

        return oldValue;
    }

    public V remove(T key) throws Exception {
        V oldValue = tree.remove(key);
        if(oldValue == null) throw new Exception("There is no element with that key");

        size--;

        return oldValue;
    }

    public V get(T key) {
        return tree.getValue(key);
    }

    public void putAll(TreeMap<T, V> map) {
        map.tree.preOrderVisit(map.tree.getRoot(), n -> tree.put(n.key, n.value));
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }
}
