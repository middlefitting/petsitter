package com.kt.petsitter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petcaretime")
public class PetCareTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CateTime cateTime;
    private String weekday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @Builder
    public PetCareTime(CateTime cateTime, String weekday, PetSitter petSitter) {
        this.cateTime = cateTime;
        this.weekday = weekday;
        this.petSitter = petSitter;
    }

    public enum CateTime {
        H0001, H0102, H0203, H0304, H0405, H0506, H0607, H0708, H0809, H0910,
        H1011, H1112, H1213, H1314, H1415, H1516, H1617, H1718, H1819, H1920,
        H2021, H2122, H2223, H2324
    }
}
