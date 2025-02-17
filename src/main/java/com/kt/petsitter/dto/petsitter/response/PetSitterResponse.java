package com.kt.petsitter.dto.petsitter.response;

import com.kt.petsitter.entity.PetSitter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetSitterResponse {
    private Long petsitterId;
    private String name;
    private String mobile;
    private String address;
    private Boolean isAccepted;

    public static PetSitterResponse from(PetSitter petSitter) {
        return PetSitterResponse.builder()
            .petsitterId(petSitter.getId())
            .name(petSitter.getName())
            .mobile(petSitter.getMobile())
            .address(petSitter.getAddress().getCity())
            .isAccepted(petSitter.getIsaccept())
            .build();
    }
}
