package com.kt.petsitter.dto.pet.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePetRequest {
    private String name;
    private String age;
    private String imageUrl;
    private Long petsize;
    private Long petGroupTypeId;
}
