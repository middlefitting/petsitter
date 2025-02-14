// package com.kt.petsitter.entity;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.SequenceGenerator;
// import lombok.AccessLevel;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
//
// /**
//  * RoleGroup.
//  *
//  * <p>
//  *
//  * </p>
//  *
//  * @author : middlefitting
//  * @see :
//  * @since : 2025. 2. 11.
//  */
// @Getter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @SequenceGenerator(
// 	name = "ROLEGROUP_SEQ_GENERATOR",
// 	sequenceName = "ROLEGROUP_SEQ",
// 	allocationSize = 1000
// )
// public class RoleGroup extends BaseEntity {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
// 	@Column(name = "id", updatable = false, unique = true, columnDefinition = "INT")
// 	private Long id;
//
// 	@Column(name = "groupname", nullable = false, columnDefinition = "VARCHAR(255)")
// 	private String groupName;
//
//
// 	@Column(name = "description", nullable = false, columnDefinition = "VARCHAR(255)")
// 	private String description;
// }
