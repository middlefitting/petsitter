package com.kt.petsitter.repository.role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.petsitter.entity.RoleGroup;

/**
 * RoleGroupRepository.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 18.
 */
public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {
	Optional<RoleGroup> findByGroupname(String user);
}
