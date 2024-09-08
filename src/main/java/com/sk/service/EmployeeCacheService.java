package com.sk.service;

import com.sk.enums.Caches;
import com.sk.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.TextQuery;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.*;
import java.util.concurrent.locks.Lock;

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
        return employees.stream().map(obj -> Employee.builder().employeeId((Long) obj.get(0)).firstName(String.valueOf(obj.get(1))).lastName(String.valueOf(obj.get(2))).address(String.valueOf(obj.get(3))).job(String.valueOf(obj.get(4))).mobileNumber((long) obj.get(5)).build()).toList();
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

    public Employee updateEmployee(Employee updatedEmpDetails) {
        log.info("Locking employee {} Thread name {}", updatedEmpDetails.getEmployeeId(), Thread.currentThread().getId());
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        Employee currentEmpDetails = employeeCache.get(updatedEmpDetails.getEmployeeId());
        Lock lock = employeeCache.lock(updatedEmpDetails.getEmployeeId());
        try {
            if (Objects.nonNull(currentEmpDetails)) {
                lock.lock();   // Acquire the lock
                log.info("Employee {} is locked by Thread name {}", updatedEmpDetails.getEmployeeId(), Thread.currentThread().getId());
                employeeCache.put(updatedEmpDetails.getEmployeeId(), updatedEmpDetails);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating employee : " + updatedEmpDetails, ex);
        } finally {
            // Release the lock
            lock.unlock();
            log.info("Releasing lock of employee {}", updatedEmpDetails.getEmployeeId());
        }
        return employeeCache.get(updatedEmpDetails.getEmployeeId());
    }

    public Employee updateEmployeeAsync(Long employeeId) throws InterruptedException {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        Lock lock = employeeCache.lock(employeeId);

        // Thread 1: Acquires the lock and holds it
        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                log.info("Entry {} locked by thread name {}", employeeId, Thread.currentThread().getId());
                Thread.sleep(5000);  // Hold lock for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("Entry {} locked released by thread name {}", employeeId, Thread.currentThread().getId());
            }
        });

        // Thread 2: Tries to update but should time out
        Thread thread2 = new Thread(() -> {
            try {
                Employee emp = employeeCache.get(employeeId);
                emp.setAddress("Updated by thread 2");
                updateEmployee(emp);
            } catch (Exception e) {
                System.out.println("Failed to update, lock held by another thread");
            }
        });

        thread1.start();
        Thread.sleep(1000);  // Ensure thread1 holds the lock
        thread2.start();

        thread1.join();
        thread2.join();
        return employeeCache.get(employeeId);
    }

    @Override
    public Caches cache() {
        return Caches.EMP_CACHE;
    }
}
