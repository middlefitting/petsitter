package com.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.entity.id.PetSitterPetGroupTypeId;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsitter_petgrouptype")
@IdClass(PetSitterPetGroupTypeId.class)
public class PetSitterPetGroupType extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petgrouptype_id")
    private PetGroupType petGroupType;
}
