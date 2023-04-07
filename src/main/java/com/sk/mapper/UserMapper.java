package com.sk.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sk.entity.Users;
import com.sk.model.UsersModel;

@Mapper(componentModel = "spring", implementationName = "UserMapperImpl")
public interface UserMapper {

	UsersModel entityToModel(Users entity);

	Users modelToEntity(UsersModel model);

	List<UsersModel> entitiesToModels(List<Users> entities);

	List<Users> modelsToEntities(List<UsersModel> models);

}
