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
@Table(name = "petgroup")
public class PetGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupname;
    private String code;

    @OneToMany(mappedBy = "petGroup")
    private List<PetGroupType> petGroupTypes = new ArrayList<>();

    @Builder
    public PetGroup(String code, String groupname, List<PetGroupType> petGroupTypes) {
        this.code = code;
        this.groupname = groupname;
        this.petGroupTypes = petGroupTypes;
    }

    public void update(String groupname, String code) {
        this.groupname = groupname;
        this.code = code;
    }
}
