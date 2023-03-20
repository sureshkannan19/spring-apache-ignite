package com.sk.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sk.entity.Notification;
import com.sk.model.NotificationModel;

@Mapper(componentModel = "spring", implementationName = "NotificationMapperImpl")
public interface NotificationMapper {

	NotificationModel entityToModel(Notification entity);

	Notification modelToEntity(NotificationModel model);

	List<NotificationModel> entitiesToModels(List<Notification> entities);

	List<Notification> modelsToEntities(List<NotificationModel> models);

}
