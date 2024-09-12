package com.sk.service;

import com.sk.enums.Caches;
import com.sk.model.Employee;
import com.sk.model.IgniteDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class CacheService<K, V> {
    @Autowired
    @Qualifier("ignite")
    protected Ignite ignite;

    public abstract Caches cache();

    public IgniteCache<K, V> getOrCreateCache() {
        return ignite.getOrCreateCache(cache().getCacheName());
    }

    public List<V> fullTextOrFuzzySearch(TextQuery<K, V> textQuery) {
        List<V> result = new ArrayList<>();
        log.info("Query :: " + textQuery.getText());
        try (QueryCursor<Cache.Entry<K, V>> cursor = getOrCreateCache().query(textQuery)) {
            for (Cache.Entry<K, V> entry : cursor) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public List<List<?>> sqlPartialSearch(Map<String, String> keyValuePredicates, StringBuilder sql,
                                          IgniteDtoWrapper igniteDtoWrapper) {
        IgniteCache<K, V> cache = getOrCreateCache();
        Object[] val = new Object[keyValuePredicates.size()];
        int index = 0;
        List<String> predicates = new ArrayList<>();
        for (Map.Entry<String, String> query : keyValuePredicates.entrySet()) {
            predicates.add(query.getKey() + " like ? ");
            val[index] = "%" + query.getValue() + "%";
            index++;
        }
        sql.append(String.join(" and ", predicates));
        SqlFieldsQuery sqlQuery = new SqlFieldsQuery(sql.toString()).setArgs(val);
        log.info("Query :: " + sqlQuery.getSql());
        FieldsQueryCursor<List<?>> cursor = cache.query(sqlQuery);
        List<List<?>> result = cursor.getAll();
        igniteDtoWrapper.setCount(result.size());
        return CollectionUtils.isEmpty(result) ? Collections.emptyList() : result;
    }

    public List<List<?>> sqlPartialSearch(Map<String, String> predicates,
                                          IgniteDtoWrapper igniteDtoWrapper) {
        return sqlPartialSearch(predicates,
                new StringBuilder("select * from " + cache().getClazz().getSimpleName() + " where "),
                igniteDtoWrapper);
    }
    protected void updateCacheConfiguration() {
        CacheConfiguration<Long, Employee> clientCacheConfig = new CacheConfiguration<>(cache().getCacheName());
        clientCacheConfig.setIndexedTypes(cache().getIndexedTypes());
        clientCacheConfig.setCacheMode(CacheMode.REPLICATED);           // Override cache mode at client side
        clientCacheConfig.setAtomicityMode(cache().getAtomicityMode());  // Override atomicity mode
        clientCacheConfig.setBackups(1);
        ignite.addCacheConfiguration(clientCacheConfig);
        ignite.getOrCreateCache(clientCacheConfig);
    }

}
