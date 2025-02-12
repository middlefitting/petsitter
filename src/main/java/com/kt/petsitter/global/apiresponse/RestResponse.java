package com.kt.petsitter.global.apiresponse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RestApiResponse.
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
public class RestResponse<T> {
	private Status status;
	private String message;
	private T data;

	public enum Status {
		SUCCESS,
		ERROR,
	}

	@Builder
	public RestResponse(T data, String message, Status status) {
		this.data = data;
		this.message = message;
		this.status = status;
	}
}
