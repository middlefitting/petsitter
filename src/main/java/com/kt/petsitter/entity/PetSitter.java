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
@Table(name = "petsitter")
public class PetSitter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mobile;
    private String introduce;
    private String experience;
    private String profileImageUrl;
    private Boolean isaccept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetCareTime> petCareTimes = new ArrayList<>();

    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetSitterPetService> petSitterPetServices = new ArrayList<>();

    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetSitterPetGroupType> petSitterPetGroupTypes = new ArrayList<>();

    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetSitterPetSize> petSitterPetSizes = new ArrayList<>();

    @OneToMany(mappedBy = "petSitter")
    private List<PetSitterReserve> reserves = new ArrayList<>();

    @Builder
    public PetSitter(String name, String mobile, Address address, User user, Boolean isaccept) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.user = user;
        this.isaccept = isaccept;
    }
}
