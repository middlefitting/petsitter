// package com.kt.petsitter.entity;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Embeddable;
// import lombok.AccessLevel;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
//
// /**
//  * UserRoleGroupId.
//  *
//  * <p>
//  *
//  * </p>
//  *
//  * @author : middlefitting
//  * @see :
//  * @since : 2025. 2. 11.
//  */
// @Embeddable
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @Getter
// public class UserRoleGroupId {
// 	@Column(name = "user_id", updatable = false, columnDefinition = "INT")
// 	private Long userId;
//
// 	@Column(name = "rolegroup_id", updatable = false, columnDefinition = "INT")
// 	private Long roleGroupId;
//
// 	public UserRoleGroupId(Long roleGroupId, Long userId) {
// 		this.roleGroupId = roleGroupId;
// 		this.userId = userId;
// 	}
// }
