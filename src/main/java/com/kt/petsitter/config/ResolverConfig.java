package com.kt.petsitter.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kt.petsitter.global.annotation.login.AdminLoginArgumentResolver;
import com.kt.petsitter.global.annotation.login.LoginArgumentResolver;

/**
 * ResolverConfig.
 *
 * <p>
 *
 * </p>
 *
 * @author : middlefitting
 * @see :
 * @since : 2025. 2. 13.
 */
@Configuration
public class ResolverConfig implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// LoginArgumentResolver 등록
		resolvers.add(new LoginArgumentResolver());
		resolvers.add(new AdminLoginArgumentResolver());
	}
}
