package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class PetSitterPetServiceId implements Serializable {
    private Long petService;
    private Long petSitter;
}
