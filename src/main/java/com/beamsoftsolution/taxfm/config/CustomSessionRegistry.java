package com.beamsoftsolution.taxfm.config;

import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
public class CustomSessionRegistry extends SessionRegistryImpl {
	
	@Override
	public void registerNewSession(String sessionId, Object principal) {
		// Custom logic for new session registration
		super.registerNewSession(sessionId, principal);
	}
	
	@Override
	public void removeSessionInformation(String sessionId) {
		// Custom logic for session removal
		super.removeSessionInformation(sessionId);
	}
}