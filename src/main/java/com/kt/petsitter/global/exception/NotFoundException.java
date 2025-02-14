package com.kt.petsitter.global.exception;

/**
 * ForbiddenException.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 13.
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException() {
		super("결과를 찾을 수 없습니다.");
	}
}

