package com.sk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.Users;
import com.sk.enums.Gender;

public interface UserRepository extends JpaRepository<Users, Long> {

	List<Users> findByGender(Gender gender);

}
