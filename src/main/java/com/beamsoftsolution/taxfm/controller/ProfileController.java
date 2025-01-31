package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(EndPoint.PROFILE)
public class ProfileController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping
	public String getProfile(HttpServletRequest httpServletRequest, Model model, Authentication authentication) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		
		if(authentication == null || !authentication.isAuthenticated()) {
			// Handle unauthenticated user. Redirect to login or show a default page.
			return "redirect:/login";
		}
		
		boolean isCustomer = authentication.getAuthorities().stream()
		                                   .map(GrantedAuthority::getAuthority)
		                                   .anyMatch("CUSTOMER"::equals);
		
		if(!isCustomer) {
			Employee employee = employeeService.getLoggedInEmployee();
			if(employee != null) {
				model.addAttribute("employee", employee);
				model.addAttribute("loggedInEmployeeId", employee.getEmployeeId());
				return "/home/employee-profile";
			}
			else {
				// Handle the case where getLoggedInEmployee() returns null. This is important!
				// Log the error, redirect, or show an error page.
				// Example:
				log.error("Employee not found after successful authentication!"); // Assuming you have a logger
				return "redirect:/login-error"; // Or a specific error page
			}
		}
		else {
			Customer customer = customerService.getLoggedInCustomer();
			if(customer != null) {
				model.addAttribute("customer", customer);
				return "/home/customer-profile";
			}
			else {
				// Handle the case where getLoggedInCustomer() returns null
				log.error("Customer not found after successful authentication!");
				return "redirect:/login-error";
			}
		}
	}
}