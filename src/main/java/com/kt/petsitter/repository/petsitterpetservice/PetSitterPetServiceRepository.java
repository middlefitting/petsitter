package com.kt.petsitter.repository.petsitterpetservice;

import com.kt.petsitter.entity.PetSitterPetService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetSitterPetServiceRepository extends JpaRepository<PetSitterPetService, Long> {
}
