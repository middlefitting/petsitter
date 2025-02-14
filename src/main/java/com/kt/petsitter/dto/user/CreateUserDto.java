package com.kt.petsitter.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.petsitter.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CreateUserDto.
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserDto {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String name;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String phone;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long userId;

	public CreateUserDto(String email, String name, String password, String phone, Long userId) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.userId = userId;
	}

	@Mapper
	public interface MapStruct {
		CreateUserDto.MapStruct INSTANCE = Mappers.getMapper(CreateUserDto.MapStruct.class);

		@Mapping(source = "id", target = "userId")
		@Mapping(target = "password", ignore = true) //패스워드 노출 제외
		CreateUserDto toDto(User entity);

		User toEntity(CreateUserDto dto);
	}
}
