package com.kt.petsitter.repository.petcaretime;

import com.kt.petsitter.entity.PetCareTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetCareTimeRepository extends JpaRepository<PetCareTime, Long> {
}
