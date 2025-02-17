package com.kt.petsitter.dto.petgroup.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePetGroupRequest {
    private String groupname;
    private String code;
}
