package com.kt.petsitter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;

import jakarta.annotation.PostConstruct;

/**
 * WebConfig.
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
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload.path}")
	private String uploadPath;

	@PostConstruct
	public void init() {
		// 업로드 디렉토리가 없으면 생성
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(3600);
	}

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// 	String resourceLocation = "file:" + uploadPath + "/";
	// 	registry.addResourceHandler("/uploads/**") // 기본 URL에서 제공
	// 		.addResourceLocations(resourceLocation)
	// 		.setCachePeriod(3600)
	// 		.resourceChain(true)
	// 		.addResolver(new PathResourceResolver());
	// }

}
