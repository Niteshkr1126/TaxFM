package com.beamsoftsolution.taxfm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSessionRegistry extends SessionRegistryImpl {

	@Override
	public void registerNewSession(String sessionId, Object principal) {
		log.info(principal.toString());
		super.registerNewSession(sessionId, principal);
	}
}
