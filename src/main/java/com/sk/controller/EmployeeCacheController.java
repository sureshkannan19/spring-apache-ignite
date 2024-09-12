package com.sk.controller;

import com.sk.model.Employee;
import com.sk.model.IgniteDtoWrapper;
import com.sk.service.EmployeeCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empCache")
public class EmployeeCacheController {

    @Autowired
    private EmployeeCacheService employeeCacheService;

    @PostMapping(path = "/load")
    public ResponseEntity<Boolean> loadEmployeeCache() {
        employeeCacheService.loadEmployees();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Boolean> addEmployee(@RequestBody Employee emp) {
        employeeCacheService.addEmployee(emp);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping(path = "/destroy")
    public ResponseEntity<Boolean> destroyAndCreateEmployeeCache() {
        employeeCacheService.destroyAndCreateEmployeeCache();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(path = "/search/{text}")
    public ResponseEntity<List<Employee>> getEmployeeCache(@PathVariable("text") String text) {
        return new ResponseEntity<>(employeeCacheService.fullTextSearch(text), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Employee>> getEmployeeCacheByText(@RequestParam(value = "fieldName", required = false) String fieldName,
                                                                 @RequestParam("text") String text) {
        if (StringUtils.hasText(fieldName)) {
            return new ResponseEntity<>(employeeCacheService.fuzzyTextSearchByFieldName(fieldName, text), HttpStatus.OK);
        }
        return new ResponseEntity<>(employeeCacheService.fuzzyTextSearch(text), HttpStatus.OK);
    }

    @GetMapping(path = "/partialSearch")
    public ResponseEntity<IgniteDtoWrapper> partialTextSearch(@RequestParam("fieldName") String fieldName,
                                                              @RequestParam("text") String text) {
        return new ResponseEntity<>(employeeCacheService.partialTextSearch(fieldName, text), HttpStatus.OK);
    }

    @PostMapping(path = "/upsert")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeCacheService.updateEmployee(employee), HttpStatus.OK);
    }

    @PostMapping(path = "/employeeAsync")
    public ResponseEntity<Employee> updateEmployeeAsync() throws InterruptedException {
        return new ResponseEntity<>(employeeCacheService.updateEmployeeAsync(6L), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{empId}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Long empId) {
        employeeCacheService.delete(empId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Boolean> deleteEmployees() {
        employeeCacheService.deleteAll();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
