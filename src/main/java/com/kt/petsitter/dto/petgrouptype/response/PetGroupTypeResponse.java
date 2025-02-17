package com.kt.petsitter.dto.petgrouptype.response;

import com.kt.petsitter.entity.PetGroupType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetGroupTypeResponse {
    private Long breedId;
    private String breedName;
    private String code;

    public static PetGroupTypeResponse from(PetGroupType petGroupType) {
        return PetGroupTypeResponse.builder()
            .breedId(petGroupType.getId())
            .breedName(petGroupType.getTypename())
            .code(petGroupType.getCode())
            .build();
    }
}
