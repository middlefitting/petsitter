package com.kt.petsitter.repository.petsitter;

import com.kt.petsitter.entity.PetSitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> {
}
