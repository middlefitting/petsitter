package com.kt.petsitter.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.repository.dto.UserDto;
import com.kt.petsitter.service.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * UserV1Controller.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 12.
 */

@RestController
@RequestMapping(("/v1/users"))
@RequiredArgsConstructor
public class UserV1Controller implements UserController {

	private final String REGISTER_SUCCESS_MSG = "회원가입에 성공했습니다.";

	private final UserService userService;

	@Override
	@PostMapping("")
	public ResponseEntity<RestResponse<UserDto>> registerUser(@RequestBody UserDto user) {
		UserDto userResultDto = userService.createUser(user);

		return new ResponseEntity<>(new RestResponse<>(userResultDto, REGISTER_SUCCESS_MSG, RestResponse.Status.SUCCESS), HttpStatus.CREATED);
	}

	@Override
	@PostMapping("/login/email")
	public ResponseEntity<RestResponse<UserDto>> loginUser(@RequestBody UserDto user) {

		return null;
	}

	@Override
	@PutMapping("/{id}")
	public ResponseEntity<RestResponse<Void>> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
		return null;
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable Long id) {
		return null;
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<RestResponse<Void>> getUserById(@PathVariable Long id) {
		return null;
	}

	@Override
	@PatchMapping("/{id}/reset-password")
	public ResponseEntity<RestResponse<Void>> resetPassword(@PathVariable Long id) {
		return null;
	}
}
