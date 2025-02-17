package com.kt.petsitter.repository.role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.petsitter.entity.UserRoleGroup;

/**
 * UserRoleGroupRepository.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 18.
 */
public interface UserRoleGroupRepository extends JpaRepository<UserRoleGroup, Long>
{
	List<UserRoleGroup> findAllByUserId(Long id);
}
