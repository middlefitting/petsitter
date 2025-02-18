package com.kt.petsitter.controller.user;

import static com.kt.petsitter.global.constant.GlobalString.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.petsitter.dto.user.CreateUserDto;
import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.dto.user.PasswordChangeUserDto;
import com.kt.petsitter.dto.user.UpdateUserInfoDto;
import com.kt.petsitter.dto.user.WithdrawUserDto;
import com.kt.petsitter.global.annotation.login.Login;
import com.kt.petsitter.global.apiresponse.RestResponse;
import com.kt.petsitter.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
@Validated
public class UserV1Controller {

	private final UserService userService;

	@PostMapping("")
	public ResponseEntity<RestResponse<CreateUserDto>> registerUser(@Valid @RequestBody CreateUserDto userReqDto) {

		CreateUserDto userResDto = userService.createUser(userReqDto);

		return new ResponseEntity<>(RestResponse.success(userResDto, REGISTER), HttpStatus.CREATED);
	}

	@PostMapping("/login/email")
	public ResponseEntity<RestResponse<EmailLoginUserDto>> loginUser(@RequestBody EmailLoginUserDto loginUserDto,
		HttpServletRequest request) {

		EmailLoginUserDto emailLoginUserDto = userService.emailLogin(loginUserDto, request.getSession(true));

		return new ResponseEntity<>(RestResponse.success(emailLoginUserDto, LOGIN), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RestResponse<UpdateUserInfoDto>> updateUser(
		@PathVariable Long id,
		@RequestBody UpdateUserInfoDto updateDto,
		@Login EmailLoginUserDto login) {

		UpdateUserInfoDto result = userService.updateUserInfo(id, updateDto, login);
		return new ResponseEntity<>(RestResponse.success(result, INFO_UD), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable Long id, @Login EmailLoginUserDto login,
		@RequestBody WithdrawUserDto withdrawDto) {
		userService.withdrawUser(id, withdrawDto, login);
		return new ResponseEntity<>(RestResponse.success(null, USER_DEL), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RestResponse<Void>> getUserById(@PathVariable Long id, @Login EmailLoginUserDto sessionUser) {
		return null;
	}

	@PatchMapping("/{id}/reset-password")
	public ResponseEntity<RestResponse<Void>> resetPassword(@PathVariable Long id,
		@RequestBody PasswordChangeUserDto passwordDto, @Login EmailLoginUserDto login) {
		userService.changePassword(id, passwordDto, login);

		return new ResponseEntity<>(RestResponse.success(null, PW_CG), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<RestResponse<Void>> logout(HttpSession session) {
		session.invalidate();  // 세션 무효화
		return ResponseEntity.ok(RestResponse.success(null, "로그아웃 되었습니다."));
	}

	@GetMapping("/check-login")
	public ResponseEntity<RestResponse<EmailLoginUserDto>> checkLogin(@Login EmailLoginUserDto sessionUser) {
		if (sessionUser == null) {
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.ok(RestResponse.success(sessionUser, "로그인 상태입니다."));
	}
}
