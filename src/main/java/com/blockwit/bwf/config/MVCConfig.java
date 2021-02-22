package com.blockwit.bwf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	private final AppInterceptor appInterceptor;

	public MVCConfig(AppInterceptor appInterceptor) {
		this.appInterceptor = appInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.addCacheMapping(CacheControl.maxAge(365, TimeUnit.DAYS)
			.noTransform()
			.mustRevalidate(), "/css/*", "/js/**", "/img/**", "/webjars/**");
		registry.addInterceptor(interceptor);

		registry.addInterceptor(appInterceptor).addPathPatterns("/app/**", "/panel/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setOneIndexedParameters(true);
		argumentResolvers.add(resolver);
	}

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/api/**")
//			.allowedOrigins("http://localhost:3000")
//			.allowCredentials(false).maxAge(3600);
//	}

}
