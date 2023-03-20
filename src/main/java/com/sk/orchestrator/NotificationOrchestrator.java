package com.sk.orchestrator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sk.model.NotificationModel;
import com.sk.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationOrchestrator {

	private final NotificationService notificationService;

	public List<NotificationModel> findAllByContent(String keyWord) {
		return notificationService.findAllByContent(keyWord);
	}

	public List<NotificationModel> updateAll(List<NotificationModel> models) {
		return notificationService.updateAll(models);
	}

}
