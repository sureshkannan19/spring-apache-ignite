package com.sk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.UserS;
import com.sk.enums.Gender;

public interface UserRepository extends JpaRepository<UserS, Long> {

	List<UserS> findByGender(Gender gender);

}
