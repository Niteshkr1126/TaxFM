package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
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
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	public String getProfile(Model model, Authentication authentication) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		if(authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}
		
		boolean isCustomer = authentication.getAuthorities().stream()
		                                   .map(GrantedAuthority::getAuthority)
		                                   .anyMatch("ROLE_CUSTOMER"::equals);
		
		if(!isCustomer) {
			Employee loggedInEmployee = employeeService.getLoggedInEmployee();
			if(loggedInEmployee != null) {
				Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
				loggedInEmployee.setSupervisor(employeeService.getSupervisor(loggedInEmployeeId));
				loggedInEmployee.setSubordinates(employeeService.getSubordinates(loggedInEmployeeId));
				loggedInEmployee.setAssignedCustomers(employeeService.getLoggedInEmployee().getAssignedCustomers());
				model.addAttribute("employee", loggedInEmployee);
				model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
				return "/home/employee-profile";
			}
			else {
				// Handle the case where getLoggedInEmployee() returns null. This is important!
				// Log the error, redirect, or show an error page.
				log.error("Employee not found after successful authentication!");
				return "redirect:/login-error";
			}
		}
		else {
			Customer loggedInCustomer = customerService.getLoggedInCustomer();
			if(loggedInCustomer != null) {
				Integer loggedInCustomerId = loggedInCustomer.getCustomerId();
				loggedInCustomer.setServices(customerService.getLoggedInCustomer().getServices());
				model.addAttribute("customer", loggedInCustomer);
				model.addAttribute("loggedInCustomerId", loggedInCustomerId);
				return "/home/customer-profile";
			}
			else {
				// Handle the case where getLoggedInCustomer() returns null. This is important!
				// Log the error, redirect, or show an error page.
				log.error("Customer not found after successful authentication!");
				return "redirect:/login-error";
			}
		}
	}
}