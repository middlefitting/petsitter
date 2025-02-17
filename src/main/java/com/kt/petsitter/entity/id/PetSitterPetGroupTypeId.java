package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class PetSitterPetGroupTypeId implements Serializable {
    private Long petSitter;
    private Long petGroupType;
}
