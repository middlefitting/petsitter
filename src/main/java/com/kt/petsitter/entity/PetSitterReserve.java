package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petsitter_reserve")
public class PetSitterReserve extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isaccept;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private Long price;
    private Long petsize;
    private String review;
    private String petname;
    private String petage;
    private String petimageurl;
    private String ispaied;
    private Long id2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petservice_id")
    private PetService petService;

    @OneToMany(mappedBy = "reserve")
    private List<PetSitterOrder> petSitterOrders = new ArrayList<>();
}
