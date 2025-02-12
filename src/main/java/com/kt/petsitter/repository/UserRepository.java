package com.kt.petsitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.petsitter.entity.User;

/**
 * UserRepository.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 12.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
