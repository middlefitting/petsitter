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
	private String status;
	private String message;
	private T data;

	private static final String SUCCESS = "SUCCESS";
	private static final String ERROR = "ERROR";

	public RestResponse(T data, String message, String status) {
		this.data = data;
		this.message = message;
		this.status = status;
	}

	public static <T> RestResponse<T> success(T data, String msg) {
		return new RestResponse<T>(data, msg, SUCCESS);
	}

	public static <T> RestResponse<T> error(T data, String msg) {
		return new RestResponse<T>(data, msg, ERROR);
	}
}
