package com.sk.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.UpdateTimestamp;

import com.sk.enums.Gender;

import lombok.Data;

@Entity
@Data
public class UserS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String dateOfBirth;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@UpdateTimestamp
	private Date lastLoginTs;

}
