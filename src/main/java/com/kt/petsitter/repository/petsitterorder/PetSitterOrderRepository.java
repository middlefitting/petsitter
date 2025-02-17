package com.kt.petsitter.repository.petsitterorder;

import com.kt.petsitter.entity.PetSitterOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetSitterOrderRepository extends JpaRepository<PetSitterOrder, Long> {
    Optional<PetSitterOrder> findByReserveId(Long reserveId);
}
