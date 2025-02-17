package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petgrouptype")
public class PetGroupType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typename;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petgroup_id")
    private PetGroup petGroup;

    @OneToMany(mappedBy = "petGroupType")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "petGroupType")
    private List<PetSitterPetGroupType> petSitterPetGroupTypes = new ArrayList<>();

    @Builder
    public PetGroupType(String code, PetGroup petGroup, List<Pet> pets,
        List<PetSitterPetGroupType> petSitterPetGroupTypes,
        String typename) {
        this.code = code;
        this.petGroup = petGroup;
        this.pets = pets;
        this.petSitterPetGroupTypes = petSitterPetGroupTypes;
        this.typename = typename;
    }

    public void update(String typename) {
        this.typename = typename;
    }
}
