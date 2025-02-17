package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsitter_petgrouptype")
public class PetSitterPetGroupType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petgrouptype_id")
    private PetGroupType petGroupType;

    @Builder
    public PetSitterPetGroupType(PetSitter petSitter, PetGroupType petGroupType) {
        this.petSitter = petSitter;
        this.petGroupType = petGroupType;
    }
}
