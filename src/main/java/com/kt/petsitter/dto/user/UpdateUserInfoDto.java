package com.kt.petsitter.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.kt.petsitter.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDto {

    private String name;

    private String phone;

    @Mapper
    public interface MapStruct {
        MapStruct INSTANCE = Mappers.getMapper(MapStruct.class);

        User toEntity(UpdateUserInfoDto dto);
        UpdateUserInfoDto toDto(User entity);
    }
}
