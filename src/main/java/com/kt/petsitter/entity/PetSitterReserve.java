package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String petname;
    private String petage;
    private String petimageurl;
    private Boolean isaccept;
    private Long price;
    private String review;
    private Boolean ispaied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petservice_id")
    private PetService petService;

    @OneToMany(mappedBy = "reserve")
    private List<PetSitterOrder> petSitterOrders = new ArrayList<>();

    @Builder
    public PetSitterReserve(LocalDateTime beginTime, LocalDateTime endTime, Boolean isaccept, Boolean ispaied, Pet pet,
        String petage, String petimageurl, String petname, PetService petService, PetSitter petSitter,
        List<PetSitterOrder> petSitterOrders, Long price, String review, User user) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.isaccept = isaccept;
        this.ispaied = ispaied;
        this.pet = pet;
        this.petage = petage;
        this.petimageurl = petimageurl;
        this.petname = petname;
        this.petService = petService;
        this.petSitter = petSitter;
        this.petSitterOrders = petSitterOrders;
        this.price = price;
        this.review = review;
        this.user = user;
    }
}
