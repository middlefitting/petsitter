package com.kt.petsitter.repository.petgrouptype;

import com.kt.petsitter.entity.PetGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetGroupTypeRepository extends JpaRepository<PetGroupType, Long> {
    List<PetGroupType> findByPetGroupId(Long petGroupId);
}
