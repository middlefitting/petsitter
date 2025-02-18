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
public class UnAuthException extends RuntimeException {

	public UnAuthException() {
		super("로그인 후 이용해주세요.");
	}
}

