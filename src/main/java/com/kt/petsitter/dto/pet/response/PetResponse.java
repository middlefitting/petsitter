package com.kt.petsitter.dto.pet.response;

import com.kt.petsitter.entity.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private String age;
    private String imageUrl;
    private Long petsize;
    private String petGroupName;
    private String petTypeName;

    public static PetResponse from(Pet pet) {
        return PetResponse.builder()
            .id(pet.getId())
            .name(pet.getName())
            .age(pet.getAge())
            .imageUrl(pet.getImageUrl())
            .petsize(pet.getPetsize())
            .petGroupName(pet.getPetGroupType().getPetGroup().getGroupname())
            .petTypeName(pet.getPetGroupType().getTypename())
            .build();
    }
}
