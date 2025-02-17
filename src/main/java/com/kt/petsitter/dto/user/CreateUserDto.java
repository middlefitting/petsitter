package com.kt.petsitter.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.petsitter.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

	@Email(message = "유효한 이메일 주소를 입력하세요.")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	private String email;

	@Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	private String password;

	@Size(min = 2, message = "이름은 2자 이상이어야 합니다.")
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	private String name;

	@Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식을 입력하세요.")
	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
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
