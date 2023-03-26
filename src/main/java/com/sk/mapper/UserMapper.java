package com.sk.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sk.entity.UserS;
import com.sk.model.UserModel;

@Mapper(componentModel = "spring", implementationName = "UserMapperImpl")
public interface UserMapper {

	UserModel entityToModel(UserS entity);

	UserS modelToEntity(UserModel model);

	List<UserModel> entitiesToModels(List<UserS> entities);

	List<UserS> modelsToEntities(List<UserModel> models);

}
