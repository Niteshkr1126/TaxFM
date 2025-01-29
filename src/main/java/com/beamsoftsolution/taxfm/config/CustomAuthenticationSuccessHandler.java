package com.beamsoftsolution.taxfm.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String username = authentication.getName();
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		// Logic to determine login type based on authentication or request parameters
		String loginType = request.getParameter("loginType");  // Get loginType from the form submission (e.g., "employee" or "customer")
		
		// You can also retrieve the login type from the roles or authorities of the user if necessary
		if(loginType == null) {
			loginType = "employee"; // Default to employee if not specified
		}
		request.getSession().setAttribute("loginType", loginType);
		response.sendRedirect("/home");
	}
}

