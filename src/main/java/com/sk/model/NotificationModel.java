package com.sk.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel {

	private Long feedId;
	private String headline;
	private String content;
	private Date lastEditedDate;
}
