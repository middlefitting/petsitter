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
public class IllegalException extends RuntimeException {

	public IllegalException() {
		super("입력값이 잘못되었습니다.");
	}

	public IllegalException(String message) {
		super(message);
	}
}

