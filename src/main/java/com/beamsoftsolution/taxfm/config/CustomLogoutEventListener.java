package com.beamsoftsolution.taxfm.config;

import com.beamsoftsolution.taxfm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutEventListener implements ApplicationListener<LogoutSuccessEvent> {
	
	@Autowired
	AttendanceService attendanceService;
	
	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		// Get the username from the SecurityContext
		String username = event.getAuthentication().getName();
		
		// Save the logout time for the user
		attendanceService.saveLogoutTime(username);
	}
}