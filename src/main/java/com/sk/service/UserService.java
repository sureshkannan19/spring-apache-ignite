package com.sk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sk.enums.Gender;
import com.sk.mapper.UserMapper;
import com.sk.model.UserModel;
import com.sk.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final UserMapper userMapper;

	public List<UserModel> findByGender(Gender gender) {
		return userMapper.entitiesToModels(userRepo.findByGender(gender));
	}

}
