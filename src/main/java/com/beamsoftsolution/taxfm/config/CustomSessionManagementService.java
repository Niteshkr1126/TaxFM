package com.beamsoftsolution.taxfm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomSessionManagementService {

	@Autowired
	private SessionRegistry sessionRegistry;

	public void invalidateUserSessions(String username) {
		// Retrieve the user's sessions
		List<Object> principals = sessionRegistry.getAllPrincipals();
		log.info(principals.toString());
		for(Object principal : principals) {
			if(principal instanceof UserDetails userDetails) {
				if(userDetails.getUsername().equals(username)) {
					List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
					for(SessionInformation session : sessions) {
						session.expireNow();
					}
				}
			}
		}
	}
}
