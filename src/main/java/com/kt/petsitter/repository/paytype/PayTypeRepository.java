package com.kt.petsitter.repository.paytype;

import com.kt.petsitter.entity.PayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayTypeRepository extends JpaRepository<PayType, Long> {
    Optional<PayType> findByType(String type);
}
