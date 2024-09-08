package com.sk.service;

import com.sk.enums.Caches;
import com.sk.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.TextQuery;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmployeeCacheService extends CacheService<Long, Employee> {

    public void loadEmployee() {
        Map<Long, Employee> employees = new HashMap<>();
        employees.put(1L, new Employee(1L, "Cristiano", "Ronaldo", "No 24, Portugal", "Footballer", 777777));
        employees.put(2L, new Employee(2L, "Virat", "Kohli", "No 24, India", "Cricketer", 181818));
        employees.put(3L, new Employee(3L, "Bruce", "Wayne", "No 24, Gotham, Wayne Enterprises", "Fight against crime", 11111));
        employees.put(4L, new Employee(4L, "Barry", "Allen", "No 24, Central city", "Fight against crime", 222222));
        employees.put(5L, new Employee(5L, "Dean", "Winchester", "No 24, Kansas, USA", "Demon hunter", 77777));
        employees.put(6L, new Employee(6L, "Billy", "Butcher", "No 24, New york, USA", "Superhero hunter", 333333));
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        employeeCache.putAll(employees);
    }

    public List<Employee> searchIndex(String text) {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        TextQuery<Long, Employee> textQuery = new TextQuery<>(Employee.class, text);
        return searchEmployees(employeeCache, textQuery);
    }

    public List<Employee> fullTextSearch(String text) {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        TextQuery<Long, Employee> textQuery = new TextQuery<>(Employee.class, text + "~");
        return searchEmployees(employeeCache, textQuery);
    }

    public List<Employee> fullTextSearchByFieldName(String fieldName, String text) {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        TextQuery<Long, Employee> textQuery = new TextQuery<>(Employee.class, fieldName + ":" + text + "~");
        return searchEmployees(employeeCache, textQuery);
    }

    public List<Employee> partialTextSearch(String fieldName, String text) {
        List<List<?>> employees = sqlQuerySearch(Map.of(fieldName, text));
        return employees.stream().map(obj -> Employee.builder()
                .employeeId((Long) obj.get(0))
                .firstName(String.valueOf(obj.get(1)))
                .lastName(String.valueOf(obj.get(2)))
                .address(String.valueOf(obj.get(3)))
                .job(String.valueOf(obj.get(4)))
                .mobileNumber((long) obj.get(5))
                .build()).toList();
    }

    private List<Employee> searchEmployees(IgniteCache<Long, Employee> igniteCache, TextQuery<Long, Employee> textQuery) {
        List<Employee> employees = new ArrayList<>();
        log.info("Query :: " + textQuery.getText());
        try (QueryCursor<Cache.Entry<Long, Employee>> cursor = igniteCache.query(textQuery)) {
            for (Cache.Entry<Long, Employee> entry : cursor) {
                employees.add(entry.getValue());
            }
        }
        return employees;
    }


    @Override
    public Caches cache() {
        return Caches.EMP_CACHE;
    }
}
