package com.kt.petsitter.dto.petgrouptype.response;

import com.kt.petsitter.entity.PetGroupType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetGroupTypeResponse {
    private Long id;
    private String typename;
    private Long petGroupId;

    public static PetGroupTypeResponse from(PetGroupType petGroupType) {
        return PetGroupTypeResponse.builder()
                .id(petGroupType.getId())
                .typename(petGroupType.getTypename())
                .petGroupId(petGroupType.getPetGroup().getId())
                .build();
    }
}
