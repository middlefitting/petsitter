package com.kt.petsitter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원정보 엔티티.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 12.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
	name = "USER_SEQ_GENERATOR",
	sequenceName = "USER_SEQ",
	allocationSize = 1000
)
@Entity
@Table(name="users")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
	@Column(name = "id", updatable = false, unique = true, columnDefinition = "INT")
	private Long id;

	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
	private String name;

	@Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
	private String password;

	@Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)", unique = true)
	private String email;

	@Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(255)")
	private String phone;

	@Column(name = "isban", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean isBan;

	@Builder
	public User(String email, String name, String password, String phone) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.isBan = false;
	}

	public User resetPassword(String newPassword) {
		this.password = newPassword;
		return this;
	}

	public User updateInfo(String name, String phone) {
		this.name = name;
		this.phone = phone;
		return this;
	}

	public User withdraw() {
		super.isDeleted = true;
		return this;
	}
}

