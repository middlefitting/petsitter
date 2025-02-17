package com.kt.petsitter.repository.petservice;

import com.kt.petsitter.entity.PetService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetServiceRepository extends JpaRepository<PetService, Long> {
}
