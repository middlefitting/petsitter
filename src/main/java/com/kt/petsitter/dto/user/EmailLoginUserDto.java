package com.kt.petsitter.dto.user;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.petsitter.entity.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Setter
public class EmailLoginUserDto {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String email;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long userId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String name;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String phone;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<RoleGroupRes> roleGroups = new ArrayList<>();

	@Data
	public static class RoleGroupRes {
		Long roleGroupId;
		String roleGroupName;

		public RoleGroupRes(Long id, String groupname) {
			this.roleGroupId = id;
			this.roleGroupName = groupname;
		}
	}

	public EmailLoginUserDto(String email, String name, String password, String phone, List<RoleGroupRes> roleGroups, Long userId) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.roleGroups = roleGroups;
		this.userId = userId;
	}

	@Mapper
	public interface MapStruct {
		EmailLoginUserDto.MapStruct INSTANCE = Mappers.getMapper(EmailLoginUserDto.MapStruct.class);

		@Mapping(source = "id", target = "userId")
		@Mapping(target = "password", ignore = true) //패스워드 노출 제외
		EmailLoginUserDto toDto(User entity);
	}
}
