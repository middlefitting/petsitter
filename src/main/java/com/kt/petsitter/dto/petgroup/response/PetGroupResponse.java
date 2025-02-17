package com.kt.petsitter.dto.petgroup.response;

import com.kt.petsitter.entity.PetGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetGroupResponse {
    private Long id;
    private String groupname;
    private String code;

    public static PetGroupResponse from(PetGroup petGroup) {
        return PetGroupResponse.builder()
                .id(petGroup.getId())
                .groupname(petGroup.getGroupname())
                .code(petGroup.getCode())
                .build();
    }
}
