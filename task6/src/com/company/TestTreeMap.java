package com.company;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TestTreeMap {
    @Test
    public void testPutAndGet() {
        TreeMap<String, Integer> map = new TreeMap<>();
        String key = "Key";
        Integer value = 210;
        map.put(key, value);

        Assert.assertSame(map.get(key), value);
    }

    @Test
    public void testSize() throws Exception {
        TreeMap<String, Integer> map = new TreeMap<>();

        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
        map.put("key4", 4);
        map.put("key5", 5);
        Assert.assertEquals(map.size(), 5);

        map.remove("key1");
        map.remove("key2");
        map.remove("key3");
        Assert.assertEquals(map.size(), 2);
    }

    @Test
    public void testIsEmpty() {
        TreeMap<String, Integer> map = new TreeMap<>();

        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
        map.put("key4", 4);
        map.put("key5", 5);
        map.clear();

        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testGetSets() {
        TreeMap<String, Integer> testMap = new TreeMap<>();
        testMap.put("key1", 0);
        testMap.put("key2", 1);
        testMap.put("key3", 2);
        testMap.put("key4", 3);
        testMap.put("key5", 4);

        ArrayList<String> keys = testMap.keySet();
        ArrayList<Integer> values = testMap.values();

        for (int i = 0; i < keys.size(); i++) {
            Assert.assertEquals(testMap.get(keys.get(i)), values.get(i));
        }
    }

    @Test
    public void testPutAll() {
        TreeMap<String, Integer> testMap = new TreeMap<>();
        testMap.put("key1", 0);
        testMap.put("key2", 1);
        testMap.put("key3", 2);
        testMap.put("key4", 3);
        testMap.put("key5", 4);


        TreeMap<String, Integer> tempMap = new TreeMap<>();
        tempMap.put("key1", 10);
        tempMap.put("key6", 5);

        testMap.putAll(tempMap);

        Assert.assertSame(10, testMap.get("key1"));
        Assert.assertSame(5, testMap.get("key6"));
    }

    @Test
    public void testWorkWithBigData() {
        TreeMap<Integer, Integer> testMap = new TreeMap<>();
        Integer[] values = new Integer[1000000];
        Integer[] keys = new Integer[1000000];

        for (int i = 0; i < 1000000; i++) {
            keys[i] = i;
            values[i] = new Random().nextInt();
            testMap.put(keys[i], values[i]);
        }

        for (int i = 0; i < 1000000; i++) {
            Assert.assertSame(testMap.get(keys[i]), values[i]);
        }
    }
}
