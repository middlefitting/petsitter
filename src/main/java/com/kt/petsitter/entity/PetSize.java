package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsize")
public class PetSize extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sizeInfo;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "productgrouptype_id")
    // private PetGroupType productGroupType;

    @OneToMany(mappedBy = "petSize")
    private List<PetSitterPetSize> petSitterPetSizes = new ArrayList<>();
}
