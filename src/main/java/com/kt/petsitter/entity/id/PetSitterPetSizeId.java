package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class PetSitterPetSizeId implements Serializable {
    private Long petSize;
    private Long petSitter;
}
