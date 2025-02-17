package com.kt.petsitter.dto.petsize.response;

import com.kt.petsitter.entity.PetSize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetSizeResponse {
    private Long id;
    private String sizeInfo;

    public static PetSizeResponse from(PetSize petSize) {
        return PetSizeResponse.builder()
            .id(petSize.getId())
            .sizeInfo(petSize.getSizeInfo())
            .build();
    }
}
