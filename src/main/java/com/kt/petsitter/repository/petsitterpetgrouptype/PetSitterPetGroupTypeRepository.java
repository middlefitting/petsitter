package com.kt.petsitter.repository.petsitterpetgrouptype;

import com.kt.petsitter.entity.PetSitterPetGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetSitterPetGroupTypeRepository extends JpaRepository<PetSitterPetGroupType, Long> {
}
