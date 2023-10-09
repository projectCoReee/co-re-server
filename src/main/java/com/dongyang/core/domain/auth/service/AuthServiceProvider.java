package com.dongyang.core.domain.auth.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.dongyang.core.domain.auth.service.impl.KakaoAuthService;
import com.dongyang.core.domain.member.MemberSocialType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider {

	private static final Map<MemberSocialType, AuthService> authServiceMap = new HashMap<>();

	private final KakaoAuthService kakaoAuthService;

	@PostConstruct
	void initializeAuthServicesMap() {
		authServiceMap.put(MemberSocialType.KAKAO, kakaoAuthService);
	}

	public AuthService getAuthService(MemberSocialType socialType) {
		return authServiceMap.get(socialType);
	}
}
