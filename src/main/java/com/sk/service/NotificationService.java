package com.sk.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sk.entity.Notification;
import com.sk.mapper.NotificationMapper;
import com.sk.model.NotificationModel;
import com.sk.repo.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notifcationRepo;
	private final NotificationMapper mapper;

	public List<NotificationModel> findAllByContent(String keyword) {
		return mapper.entitiesToModels(notifcationRepo.findByContentContaining(keyword));
	}

	public List<NotificationModel> updateAll(List<NotificationModel> models) {
		return mapper.entitiesToModels(notifcationRepo.saveAll(mapper.modelsToEntities(models)));
	}

	public NotificationModel getFeedById(Long id) {
		Optional<Notification> result = notifcationRepo.findById(id);
		if (result.isPresent()) {
			return mapper.entityToModel(result.get());
		}
		return new NotificationModel();
	}

}
