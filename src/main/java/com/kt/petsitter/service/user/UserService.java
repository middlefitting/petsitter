package com.kt.petsitter.service.user;

import static com.kt.petsitter.global.constant.GlobalString.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.petsitter.dto.user.EmailLoginUserDto;
import com.kt.petsitter.dto.user.PasswordChangeUserDto;
import com.kt.petsitter.entity.User;
import com.kt.petsitter.global.exception.ForbiddenException;
import com.kt.petsitter.global.exception.IllegalException;
import com.kt.petsitter.dto.user.CreateUserDto;
import com.kt.petsitter.dto.user.UpdateUserInfoDto;
import com.kt.petsitter.dto.user.WithdrawUserDto;
import com.kt.petsitter.repository.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * UserService.
 *
 * <p>
 *
 * </p>
 * @see             :
 * @author          : middlefitting
 * @since           : 2025. 2. 12.
 */
@Service
@RequiredArgsConstructor
public class UserService {


	private final UserRepository userRepository;

	public CreateUserDto createUser(CreateUserDto reqUserDto) {
		User user = CreateUserDto.MapStruct.INSTANCE.toEntity(reqUserDto);
		userRepository.save(user);
		return CreateUserDto.MapStruct.INSTANCE.toDto(user);
	}

	public EmailLoginUserDto emailLogin(EmailLoginUserDto loginUserDto, HttpSession httpSession) {

		User user = userRepository.findByEmailAndPassword(loginUserDto.getEmail(), loginUserDto.getPassword())
			.orElseThrow(IllegalException::new);

		EmailLoginUserDto emailLoginUserDto = EmailLoginUserDto.MapStruct.INSTANCE.toDto(user);

		//TODO: 권한기능 추가 이후 DTO에 권한관련 데이터 추가

		//TODO: JWT로 전환 및 암호화 고려
		httpSession.setAttribute(USER_SESSION, emailLoginUserDto);

		return emailLoginUserDto;
	}

	@Transactional
	public void changePassword(Long id, PasswordChangeUserDto passwordDto, EmailLoginUserDto login) {
		if (!id.equals(login.getUserId())) {
			throw new ForbiddenException();
		}

		userRepository.findByIdAndPassword(id, passwordDto.getPassword())
			.orElseThrow(IllegalException::new)
			.resetPassword(passwordDto.getNewPassword());

		// userRepository.save(user);
	}

	@Transactional
	public UpdateUserInfoDto updateUserInfo(Long id, UpdateUserInfoDto updateDto, EmailLoginUserDto login) {
		if (!id.equals(login.getUserId())) {
			throw new ForbiddenException();
		}

		User existingUser = userRepository.findById(id)
			.orElseThrow(IllegalException::new);

		existingUser.updateInfo(updateDto.getName(), updateDto.getPhone());

		return UpdateUserInfoDto.MapStruct.INSTANCE.toDto(existingUser);
	}

	@Transactional
	public void withdrawUser(Long id, WithdrawUserDto withdrawDto, EmailLoginUserDto login) {
		if (!id.equals(login.getUserId())) {
			throw new ForbiddenException();
		}

		User user = userRepository.findByIdAndIsDeleted(id, false)
			.orElseThrow(IllegalException::new);

		user.withdraw();
	}
}
