package com.sk.orchestrator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sk.enums.Gender;
import com.sk.model.UsersModel;
import com.sk.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserOrchestrator {

	private final UserService userService;

	public List<UsersModel> findByGender(String gender) {
		return userService.findByGender(Gender.findByCode(gender));
	}
}
