package de.plushnikov.constructor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public class Java8GenericConstructor {

    @AllArgsConstructor
    @RequiredArgsConstructor
    private static final class Entity<K, V> {
        private final K key;
        V value;
    }

    public static void main(String[] args) {
        Entity<String, Integer> key = new Entity<>("Key", 100);
        Entity<String, Integer> key2 = new Entity<>("Key2");
        System.out.println(key);
        System.out.println(key2);
    }
}
