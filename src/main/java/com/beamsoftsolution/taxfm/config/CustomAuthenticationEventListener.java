package com.beamsoftsolution.taxfm.config;

import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
	
	// Inject your user service/repository to check the account status
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String username = event.getAuthentication().getName();
		
		// Check if the user is disabled or locked
		if(isUserAccountDisabledOrLocked(username)) {
			// Invalidate the session if account is disabled or locked
			SecurityContextHolder.clearContext();
			// Optionally, redirect to a logout page or custom message
			// If you are using a session manager, make sure the session is invalidated.
			event.getAuthentication().setAuthenticated(false);
		}
	}
	
	private boolean isUserAccountDisabledOrLocked(String username) {
		// Check in EmployeeRepository
		Employee employeeUser = employeeRepository.findByUsername(username).orElse(null);
		if(employeeUser != null && (!employeeUser.getEnabled() || !employeeUser.getAccountNonLocked())) {
			return true;
		}
		
		// Check in CustomerRepository
		Customer customerUser = customerRepository.findByUsername(username).orElse(null);
		if(customerUser != null && (!customerUser.getEnabled() || !customerUser.getAccountNonLocked())) {
			return true;
		}
		
		return false;
	}
}

