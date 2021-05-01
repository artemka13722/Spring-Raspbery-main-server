package ru.artemka.demo.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MutableSetBuilder {
    @SafeVarargs
    public static <T> Set<T> build(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}