package com.sk.service;

import com.sk.enums.Caches;
import com.sk.model.Employee;
import com.sk.model.IgniteDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.Lock;

@Service
@Slf4j
public class EmployeeCacheService extends CacheService<Long, Employee> {

    public void loadEmployees() {
        Map<Long, Employee> employees = new HashMap<>();
        for (long i = 1; i < 10000; i++) {
            employees.put(i, new Employee(i++, "Cristiano", "Ronaldo", "No 24, Portugal", "Footballer", 777777));
            employees.put(i, new Employee(i++, "Virat", "Kohli", "No 24, India", "Cricketer", 181818));
            employees.put(i, new Employee(i++, "Bruce", "Wayne", "No 24, Gotham, Wayne Enterprises", "Fight against crime", 11111));
            employees.put(i, new Employee(i++, "Barry", "Allen", "No 24, Central city", "Fight against crime", 222222));
            employees.put(i, new Employee(i++, "Dean", "Winchester", "No 24, Kansas, USA", "Demon hunter", 77777));
            employees.put(i, new Employee(i++, "Billy", "Butcher", "No 24, New york, USA", "Superhero hunter", 333333));
        }
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        employeeCache.putAll(employees);
    }

    public void addEmployee(Employee emp) {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        employeeCache.put(emp.getEmployeeId(), emp);
    }

    public List<Employee> fullTextSearch(String text) {
        return fullTextOrFuzzySearch(new TextQuery<>(Employee.class, text));
    }

    public List<Employee> fuzzyTextSearch(String text) {
        return fullTextOrFuzzySearch(new TextQuery<>(Employee.class, text + "~"));
    }

    public List<Employee> fuzzyTextSearchByFieldName(String fieldName, String text) {
        return fullTextOrFuzzySearch(new TextQuery<>(Employee.class, fieldName + ":" + text + "~"));
    }

    public List<Employee> fullTextSearchByFieldName(String fieldName, String text) {
        return fullTextOrFuzzySearch(new TextQuery<>(Employee.class, fieldName + ":" + text));
    }

    public IgniteDtoWrapper partialTextSearch(String fieldName, String text) {
        IgniteDtoWrapper dtoWrapper = new IgniteDtoWrapper();
        dtoWrapper.setCount(0);
        List<List<?>> employees = sqlPartialSearch(Map.of(fieldName, text), dtoWrapper);
        dtoWrapper.setData(employees.stream().map(obj -> Employee.builder().employeeId((Long) obj.get(0))
                        .firstName(String.valueOf(obj.get(1))).lastName(String.valueOf(obj.get(2)))
                        .address(String.valueOf(obj.get(3))).job(String.valueOf(obj.get(4)))
                        .mobileNumber((long) obj.get(5)).build())
                .toList());
        return dtoWrapper;
    }

    public Employee updateEmployee(Employee updatedEmpDetails) {
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
            log.info("Employee {} lock is released by thread name {}", updatedEmpDetails.getEmployeeId(), Thread.currentThread().getId());
        }
        return employeeCache.get(updatedEmpDetails.getEmployeeId());
    }

    /**
     * Output of Employee entry update async :
     * //        01:39:21.329  Employee 6 is locked by thread name 117
     * //        01:39:22.330  Employee 1 is locked by Thread name 119
     * //        01:39:22.442  Employee 1 lock is released by thread name 119
     * //        01:39:26.332  Employee 6 lock is released by thread name 117
     * //        01:39:26.343  Employee 6 is locked by Thread name 118
     * //        01:39:26.352  Employee 6 lock is released by thread name 118
     *
     * @param employeeId
     * @return
     * @throws InterruptedException
     */

    public Employee updateEmployeeAsync(Long employeeId) throws InterruptedException {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        Lock lock = employeeCache.lock(employeeId);

        // Thread 1: Acquires the lock and holds it
        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                log.info("Employee {} is locked by thread name {}", employeeId, Thread.currentThread().getId());
                Thread.sleep(5000);  // Hold lock for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("Employee {} lock is released by thread name {}", employeeId, Thread.currentThread().getId());
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

        // Thread 2: Tries to update different(not - locked) entry , should execute immediately
        Thread thread3 = new Thread(() -> {
            try {
                Employee emp = employeeCache.get(1L);
                emp.setAddress("Updated by thread 3");
                updateEmployee(emp);
            } catch (Exception e) {
                System.out.println("Failed to update, lock held by another thread");
            }
        });

        thread1.start();
        Thread.sleep(1000);  // Ensure thread1 holds the lock
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        return employeeCache.get(employeeId);
    }

    public void delete(Long employeeId) {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        Employee currentEmpDetails = employeeCache.get(employeeId);
        Lock lock = employeeCache.lock(employeeId);
        try {
            if (Objects.nonNull(currentEmpDetails)) {
                lock.lock();
                log.info("Employee {} is locked by Thread name {}", employeeId, Thread.currentThread().getId());
                employeeCache.remove(employeeId);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating employee : " + employeeId, ex);
        } finally {
            // Release the lock
            lock.unlock();
            log.info("Employee {} lock is released by thread name {}", employeeId, Thread.currentThread().getId());
        }
    }

    public void deleteAll() {
        IgniteCache<Long, Employee> employeeCache = getOrCreateCache();
        employeeCache.removeAll();
    }


    @Override
    public Caches cache() {
        return Caches.EMP_CACHE;
    }

    public void destroyAndCreateEmployeeCache() {
        ignite.destroyCache(cache().getCacheName());
        updateCacheConfiguration();
        loadEmployees();
    }
}
