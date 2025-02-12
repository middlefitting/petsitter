package com.kt.petsitter.repository.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.kt.petsitter.entity.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserDto.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 12.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class UserDto {
	private Long userId;
	private String name;
	private String password;
	private String email;
	private String phone;

	public UserDto(String email, String name, String password, String phone, Long userId) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.userId = userId;
	}

	@Mapper
	public interface MapStruct {
		UserDto.MapStruct INSTANCE = Mappers.getMapper(UserDto.MapStruct.class);

		@Mapping(source = "id", target = "userId")
		UserDto toDto(User entity);

		User toEntity(UserDto dto);
	}
}
