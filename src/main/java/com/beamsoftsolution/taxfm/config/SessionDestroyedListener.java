package com.beamsoftsolution.taxfm.config;

import com.beamsoftsolution.taxfm.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {
	
	@Autowired
	AttendanceService attendanceService;
	
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		log.info("SessionDestroyedEvent received!");
		for(SecurityContext securityContext : event.getSecurityContexts()) {
			if(securityContext != null && securityContext.getAuthentication() != null) {
				Object principal = securityContext.getAuthentication().getPrincipal();
				String username = null;
				
				if(principal instanceof UserDetails) {
					username = ((UserDetails) principal).getUsername();
				}
				else if(principal instanceof String) {
					username = (String) principal; // Handle cases where principal is a String
				}
				
				if(username != null) {
					log.info("Session destroyed for user: {}", username);
				}
				else {
					log.info("Session destroyed for an unknown user.");
					log.debug("Principal object: {}", principal); // Log the principal for debugging
				}
			}
			else {
				log.info("Session destroyed with no security context.");
			}
		}
	}
}