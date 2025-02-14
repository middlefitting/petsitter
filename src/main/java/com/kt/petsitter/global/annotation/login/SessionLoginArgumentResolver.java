package com.kt.petsitter.global.annotation.login;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.kt.petsitter.dto.user.EmailLoginUserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionLoginArgumentResolver implements HandlerMethodArgumentResolver {

	// 세션에 저장된 속성명. 예: "USER_SESSION"
	private static final String USER_SESSION = "USER_SESSION";

	/**
	 * supportsParameter :
	 *  이 파라미터에 대해 이 Resolver가 동작해야 하는지 여부를 반환
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasSessionLoginAnnotation = parameter.hasParameterAnnotation(SessionLogin.class);
		boolean isUserDtoType = EmailLoginUserDto.class.isAssignableFrom(parameter.getParameterType());
		return hasSessionLoginAnnotation && isUserDtoType;
	}

	/**
	 * resolveArgument :
	 *  supportsParameter == true 라면,
	 *  실제로 세션에서 객체를 꺼내와서 컨트롤러 파라미터에 주입
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		// 세션이 없으면 null 반환 (false)
		HttpSession session = request.getSession(false);

		// 세션 자체가 없거나, 세션에 USER_SESSION 키가 없으면 401 에러
		if (session == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해 주세요.");
		}
		Object userObj = session.getAttribute(USER_SESSION);
		if (userObj == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해 주세요.");
		}

		return userObj;
	}
}
