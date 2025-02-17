package com.kt.petsitter.dto.petservice.response;

import com.kt.petsitter.entity.PetService;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetServiceResponse {
    private Long petServiceId;
    private String servicename;

    public static PetServiceResponse from(PetService petService) {
        return PetServiceResponse.builder()
            .petServiceId(petService.getId())
            .servicename(petService.getServicename())
            .build();
    }
}
