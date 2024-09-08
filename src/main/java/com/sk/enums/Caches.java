package com.sk.enums;

import com.sk.model.Employee;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Caches {

    EMP_CACHE("EMP_CACHE", Employee.class, Arrays.asList(Long.class, String.class));

    private final String cacheName;
    private final Class clazz;
    private final List<Class> indexes;

    Caches(String cacheName, Class clazz, List<Class> indexes) {
        this.cacheName = cacheName;
        this.clazz = clazz;
        this.indexes = indexes;
    }

}
