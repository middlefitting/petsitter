package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsitter_petsize")
public class PetSitterPetSize extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsize_id")
    private PetSize petSize;

    @Builder
    public PetSitterPetSize(PetSitter petSitter, PetSize petSize) {
        this.petSitter = petSitter;
        this.petSize = petSize;
    }
}
