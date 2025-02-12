package com.kt.petsitter.service.user;

import org.springframework.stereotype.Service;

import com.kt.petsitter.entity.User;
import com.kt.petsitter.repository.UserRepository;
import com.kt.petsitter.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;

/**
 * UserService.
 *
 * <p>
 *   
 * </p>
 * @see             : 
 * @author          : middlefitting
 * @since           : 2025. 2. 12.
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserDto createUser(UserDto userDto) {
		User user = UserDto.MapStruct.INSTANCE.toEntity(userDto);
		userRepository.save(user);
		return UserDto.MapStruct.INSTANCE.toDto(user);
	}
}
