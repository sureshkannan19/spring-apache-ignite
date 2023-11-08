package com.sk.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sk.model.UsersModel;
import com.sk.orchestrator.UserOrchestrator;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserOrchestrator userOrchestrator;

    @RequestMapping(path = "/protected/user/findByGender/{gender}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsersModel>> findByGender(@PathVariable(required = true, name = "gender") String gender)
            throws ParseException {
        log.info("Searching for gender {}", gender);
        return new ResponseEntity<>(userOrchestrator.findByGender(gender), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/login/{userName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersModel> logIn(@PathVariable(required = true, name = "userName") String userName, HttpServletResponse response) {
        log.info("User {}", userName);
        return userOrchestrator.logIn(userName, response);
    }
}
