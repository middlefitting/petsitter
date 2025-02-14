package com.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "petcaretime")
public class PetCareTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime begintime;
    private LocalTime endtime;
    private String weekday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2")
    private PetSitter petSitter;
}
