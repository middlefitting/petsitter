package com.kt.petsitter.dto.petgrouptype.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePetGroupTypeRequest {
    private String typename;
    private Long petGroupId;
}
