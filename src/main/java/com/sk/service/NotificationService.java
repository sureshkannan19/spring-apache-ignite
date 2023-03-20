package com.sk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sk.mapper.NotificationMapper;
import com.sk.model.NotificationModel;
import com.sk.repo.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notifcationRepo;
	private final NotificationMapper mapper;

	public List<NotificationModel> findAllByContent(String keyWord) {
		return mapper.entitiesToModels(notifcationRepo.findByContentLike(keyWord));
	}

	public List<NotificationModel> updateAll(List<NotificationModel> models) {
		return mapper.entitiesToModels(notifcationRepo.saveAll(mapper.modelsToEntities(models)));
	}

}
