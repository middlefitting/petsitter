package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsitter_petservice")
public class PetSitterPetService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petservice_id")
    private PetService petService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @Builder
    public PetSitterPetService(PetService petService, PetSitter petSitter, Long hprice) {
        this.petService = petService;
        this.petSitter = petSitter;
        this.hprice = hprice;
    }
}
