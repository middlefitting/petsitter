package com.kt.petsitter.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모든 엔티티가 기본적으로 상속받는 BaseEntity.
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
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@Column(name = "is_deleted", nullable = false, columnDefinition="BOOLEAN")
	protected Boolean isDeleted = false;

	@CreatedDate
	@Column(name = "created_date", nullable = false, updatable = false, columnDefinition="TIMESTAMP WITH TIME ZONE")
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "modified_date",  columnDefinition="TIMESTAMP WITH TIME ZONE")
	private LocalDateTime modifiedDate;

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
		this.modifiedDate = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.modifiedDate = LocalDateTime.now();
	}
}
