package com.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petservice")
public class PetService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petservice_id")
    private Long id;

    private String servicename;

    @OneToMany(mappedBy = "petService")
    private List<PetSitterPetService> petSitterPetServices = new ArrayList<>();
}
