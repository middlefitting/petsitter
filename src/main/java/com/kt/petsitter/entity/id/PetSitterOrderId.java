package com.kt.petsitter.entity.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class PetSitterOrderId implements Serializable {
    private Long reserve;
    private Long order;
}
