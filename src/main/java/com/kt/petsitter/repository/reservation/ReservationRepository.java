package com.kt.petsitter.repository.reservation;

import com.kt.petsitter.entity.PetSitterReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<PetSitterReserve, Long> {
    List<PetSitterReserve> findByPetUserId(Long userId);
}
