package in.sabnar.gateway.service;

import java.util.List;

import in.sabnar.gateway.dto.UserDto;
import in.sabnar.gateway.entity.User;

public interface UserService {
	UserDto save(UserDto user);

	List<UserDto> findAll();

	User findOne(String id);

	void delete(String id);
}
