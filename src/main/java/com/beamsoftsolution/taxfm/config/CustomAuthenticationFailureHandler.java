package com.beamsoftsolution.taxfm.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String loginType = request.getParameter("loginType");  // Get loginType from the form submission (e.g., "employee" or "customer")
		
		// You can also retrieve the login type from the roles or authorities of the user if necessary
		if(loginType == null) {
			loginType = "employee"; // Default to employee if not specified
		}
		request.getSession().setAttribute("loginType", loginType);
		log.info("Exception: " + exception);
		if(exception instanceof UsernameNotFoundException) {
			String loginErrorMessage = exception.getMessage();
			request.getSession().setAttribute("loginErrorMessage", loginErrorMessage);
			response.sendRedirect("/login-error");
		}
		else {
			response.sendRedirect("/login-error");
		}
	}
}