package com.kt.petsitter.dto.petgroup.response;

import com.kt.petsitter.entity.PetGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetGroupResponse {
    private Long groupId;
    private String groupName;
    private String code;

    public static PetGroupResponse from(PetGroup petGroup) {
        return PetGroupResponse.builder()
            .groupId(petGroup.getId())
            .groupName(petGroup.getGroupname())
            .code(petGroup.getCode())
            .build();
    }
}
