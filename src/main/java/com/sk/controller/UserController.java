package com.sk.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sk.model.UsersModel;
import com.sk.orchestrator.UserOrchestrator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

	@Autowired
	private UserOrchestrator userOrchestrator;

	@RequestMapping(path = "/user/findByGender/{gender}", method = RequestMethod.GET)
	public List<UsersModel> findByGender(@PathVariable(required = true, name = "gender") String gender)
			throws ParseException {
		log.info("Searching for gender {}", gender);
		return userOrchestrator.findByGender(gender);
	}
}
