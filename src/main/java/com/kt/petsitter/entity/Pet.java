package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pet")
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String age;
    private String imageUrl;
    private Long petsize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petgrouptype_id")
    private PetGroupType petGroupType;

    @OneToMany(mappedBy = "pet")
    private List<PetSitterReserve> reserves = new ArrayList<>();

    @Builder
    public Pet(String name, String age, String imageUrl, Long petsize, User user, PetGroupType petGroupType) {
        this.name = name;
        this.age = age;
        this.imageUrl = imageUrl;
        this.petsize = petsize;
        this.user = user;
        this.petGroupType = petGroupType;
    }

    public void update(String name, String age, String imageUrl, Long petsize, PetGroupType petGroupType) {
        this.name = name;
        this.age = age;
        this.imageUrl = imageUrl;
        this.petsize = petsize;
        this.petGroupType = petGroupType;
    }
}
