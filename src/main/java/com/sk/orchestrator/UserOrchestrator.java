package com.sk.orchestrator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sk.enums.Gender;
import com.sk.model.UserModel;
import com.sk.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserOrchestrator {

	private final UserService userService;

	public List<UserModel> findByGender(String gender) {
		return userService.findByGender(Gender.findByCode(gender));
	}
}
