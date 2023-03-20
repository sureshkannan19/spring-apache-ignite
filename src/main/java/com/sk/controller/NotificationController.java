package com.sk.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sk.model.NotificationModel;
import com.sk.orchestrator.NotificationOrchestrator;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class NotificationController {

	@Autowired
	private NotificationOrchestrator notifcationOrchestrator;

	@RequestMapping(path = "/notification/updateTodaysFeed", method = RequestMethod.POST)
	public List<NotificationModel> updateDetails(@RequestBody(required = true) List<NotificationModel> models) {
		log.info("Updating Notification...");
		models.forEach(model -> model.setLastEditedDate(new Date()));
		return notifcationOrchestrator.updateAll(models);
	}
	
	@RequestMapping(path = "/notification/getFeedByContent", method = RequestMethod.GET)
	public List<NotificationModel> getFeedByContent(@RequestParam(required = true, value="keyword") String keyword) {
		log.info("Finding feed for keyword {} ...", keyword);
		return notifcationOrchestrator.findAllByContent(keyword);
	}
}
