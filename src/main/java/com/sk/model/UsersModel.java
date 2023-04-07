package com.sk.model;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sk.enums.Gender;

import lombok.Data;

@Data
public class UsersModel {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String dateOfBirth;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private Date lastLoginTs;
}
