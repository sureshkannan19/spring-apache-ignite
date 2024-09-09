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
        employeeCacheService.loadEmployee();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(path = "/search/{text}")
    public ResponseEntity<List<Employee>> getEmployeeCache(@PathVariable("text") String text) {
        return new ResponseEntity<>(employeeCacheService.searchIndex(text), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Employee>> getEmployeeCacheByText(@RequestParam(value = "fieldName", required = false) String fieldName,
                                                                 @RequestParam("text") String text) {
        if (StringUtils.hasText(fieldName)) {
            return new ResponseEntity<>(employeeCacheService.fullTextSearchByFieldName(fieldName, text), HttpStatus.OK);
        }
        return new ResponseEntity<>(employeeCacheService.fullTextSearch(text), HttpStatus.OK);
    }

    @GetMapping(path = "/partialSearch")
    public ResponseEntity<IgniteDtoWrapper> partialTextSearch(@RequestParam("fieldName") String fieldName,
                                                              @RequestParam("text") String text) {
        return new ResponseEntity<>(employeeCacheService.partialTextSearch(fieldName, text), HttpStatus.OK);
    }

    @PostMapping(path = "/employee")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeCacheService.updateEmployee(employee), HttpStatus.OK);
    }

    @PostMapping(path = "/employeeAsync")
    public ResponseEntity<Employee> updateEmployeeAsync() throws InterruptedException {
        return new ResponseEntity<>(employeeCacheService.updateEmployeeAsync(6L), HttpStatus.OK);
    }


}
