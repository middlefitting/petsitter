package com.kt.petsitter.controller.user;

import org.springframework.http.ResponseEntity;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.repository.dto.UserDto;

/**
 * UserController.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 12.
 */
public interface UserController {

	//회원 가입
	ResponseEntity<RestResponse<UserDto>> registerUser(UserDto user);

	//로그인
	ResponseEntity<RestResponse<UserDto>> loginUser(UserDto user);

	// 회원정보 업데이트
	ResponseEntity<RestResponse<Void>> updateUser(Long id, UserDto user);

	// 회원삭제
	ResponseEntity<RestResponse<Void>> deleteUser(Long id);

	// 회원정보 조회
	ResponseEntity<RestResponse<Void>> getUserById(Long id);

	// 비밀번호 초기화
	ResponseEntity<RestResponse<Void>> resetPassword(Long id);
}
