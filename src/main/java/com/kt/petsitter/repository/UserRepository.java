package com.kt.petsitter.repository;

import java.util.Optional;

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

	Optional<User> findByEmailAndPassword(String email, String password);

	Optional<User> findByIdAndPassword(Long id, String password);

	Optional<User> findByIdAndIsDeleted(Long id, Boolean isDelete);
}
