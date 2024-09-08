package com.sk.enums;

import com.sk.model.Employee;
import lombok.Getter;
import org.apache.ignite.cache.CacheAtomicityMode;

@Getter
public enum Caches {

    EMP_CACHE("EMP_CACHE", Employee.class, new Class[]{Long.class, Employee.class}, CacheAtomicityMode.TRANSACTIONAL);

    private final String cacheName;
    private final Class clazz;
    private final Class[] indexedTypes;
    private final CacheAtomicityMode atomicityMode;

    Caches(String cacheName, Class clazz, Class[] indexedTypes, CacheAtomicityMode atomicityMode) {
        this.cacheName = cacheName;
        this.clazz = clazz;
        this.indexedTypes = indexedTypes;
        this.atomicityMode = atomicityMode;
    }

}
