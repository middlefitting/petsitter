package com.kt.petsitter.repository.petsitterpetsize;

import com.kt.petsitter.entity.PetSitterPetSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetSitterPetSizeRepository extends JpaRepository<PetSitterPetSize, Long> {
}
