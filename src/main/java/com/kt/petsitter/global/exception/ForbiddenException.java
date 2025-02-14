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
public class ForbiddenException extends RuntimeException {

	public ForbiddenException() {
		super("금지된 접근입니다.");
	}
}

