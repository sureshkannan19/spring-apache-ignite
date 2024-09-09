package com.sk.service;

import com.sk.enums.Caches;
import com.sk.model.IgniteDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class CacheService<K, V> {
    @Autowired
    @Qualifier("ignite")
    private Ignite ignite;

    public abstract Caches cache();

    public IgniteCache<K, V> getOrCreateCache() {
        return ignite.getOrCreateCache(cache().getCacheName());
    }

    public List<List<?>> sqlQuerySearch(Map<String, String> predicates, StringBuilder sql,
                                        IgniteDtoWrapper igniteDtoWrapper) {
        IgniteCache<K, V> cache = getOrCreateCache();
        Object[] val = new Object[predicates.size()];
        int index = 0;
        for (Map.Entry<String, String> query : predicates.entrySet()) {
            sql.append(query.getKey()).append(" like ? ").append(" ");
            val[index] = "%" + query.getValue() + "%";
            index++;
        }
        SqlFieldsQuery sqlQuery = new SqlFieldsQuery(sql.toString()).setArgs(val);
        log.info("Query :: " + sqlQuery.getSql());
        FieldsQueryCursor<List<?>> cursor = cache.query(sqlQuery);
        List<List<?>> result = cursor.getAll();
        igniteDtoWrapper.setCount(result.size());
        return CollectionUtils.isEmpty(result) ? Collections.emptyList() : result;
    }

    public List<List<?>> sqlQuerySearch(Map<String, String> predicates,
                                        IgniteDtoWrapper igniteDtoWrapper) {
        return sqlQuerySearch(predicates,
                new StringBuilder("select * from " + cache().getClazz().getSimpleName() + " where "),
                igniteDtoWrapper);
    }

}
