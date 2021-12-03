package com.kyobo.dev.api.Ssum;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@EnableJpaAuditing  // CommonDateEntity의 Auditing 활성화를 위해 추가
@SpringBootApplication
public class SsumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsumApplication.class, args);
	}

	// 패스워드 단방향 암호화를 지원하는 PasswordEncoder Bean 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	// Entity와 Dto 객체의 매핑을 지원하는 ModelMapper Bean 등록
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	// Http 서버 통신을 지원하는 RestTemplate Bean 등록
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
