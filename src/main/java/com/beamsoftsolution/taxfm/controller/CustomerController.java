package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.model.ServiceRate;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.service.ServiceRateService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping(EndPoint.CUSTOMERS)
public class CustomerController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ServiceRateService serviceRateService;
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('VIEW_ALL_CUSTOMERS', 'VIEW_ASSIGNED_CUSTOMERS')")
	public String getAllCustomers(Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		loggedInEmployee.setAssignedCustomers(employeeService.getLoggedInEmployee().getAssignedCustomers());
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		Set<String> loggedInAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		List<Customer> customers;
		boolean showNewCustomerAddition = false;
		
		if(loggedInAuthorities.contains("VIEW_ALL_CUSTOMERS")) {
			customers = customerService.getAllCustomers();
			// Add logic for displaying the "New Customer Addition" button
			long totalCustomersCount = customerService.getTotalCustomersCount();
			showNewCustomerAddition = totalCustomersCount < Constants.MAX_CUSTOMERS;
		}
		else if(loggedInAuthorities.contains("VIEW_ASSIGNED_CUSTOMERS")) {
			customers = loggedInEmployee.getAssignedCustomers();
		}
		else {
			return "redirect:/access-denied";
		}
		model.addAttribute("customers", customers);
		model.addAttribute("showNewCustomerAddition", showNewCustomerAddition);
		return "customers/customers";
	}
	
	@GetMapping("/{customerId}")
	@PreAuthorize("hasAnyAuthority('VIEW_ALL_CUSTOMERS', 'VIEW_ASSIGNED_CUSTOMERS')")
	public String viewCustomer(@PathVariable Integer customerId, Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		loggedInEmployee.setAssignedCustomers(employeeService.getLoggedInEmployee().getAssignedCustomers());
		Set<String> loggedInAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		
		// Fetch the requested customer
		Customer customer = customerService.getCustomerById(customerId);
		if(customer == null) {
			return "redirect:/access-denied"; // Prevents null reference issues
		}
		
		// Check if the customer is assigned to the loggedInEmployee
		boolean isAssignedCustomer = loggedInEmployee.getAssignedCustomers().stream()
		                                             .anyMatch(cust -> cust.getCustomerId().equals(customer.getCustomerId()));
		
		// Check access permissions
		if(loggedInAuthorities.contains("VIEW_ALL_CUSTOMERS") ||
				(loggedInAuthorities.contains("VIEW_ASSIGNED_CUSTOMERS") && isAssignedCustomer)) {
			// Get the list of services for the dropdown
			List<ServiceRate> services = serviceRateService.getAllServiceRates();
			model.addAttribute("availableServices", services);
			model.addAttribute("customer", customer);
			return "customers/customer";
		}
		return "redirect:/access-denied";
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('ADD_CUSTOMER')")
	public String showAddCustomer(Model model) {
		taxFMUtils.setCompanyAttributes(model);
		model.addAttribute("customer", new Customer());
		return "customers/addCustomer";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADD_CUSTOMER')")
	public String addCustomer(Customer customer, Errors errors) throws TaxFMException {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/addCustomer";
		}
		else {
			Customer addedCustomer = customerService.addCustomer(customer);
			return "redirect:/customers/" + addedCustomer.getCustomerId();
		}
	}
	
	@GetMapping("/{customerId}/edit")
	@PreAuthorize("hasAuthority('UPDATE_CUSTOMER')")
	public String updateCustomer(@PathVariable Integer customerId, Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
		return "customers/editCustomer";
	}
	
	@PostMapping("/{customerId}/edit")
	@PreAuthorize("hasAuthority('UPDATE_CUSTOMER')")
	public String updateCustomer(Customer customer, Errors errors) throws TaxFMException {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/editCustomer";
		}
		else {
			Customer updatedCustomer = customerService.updateCustomer(customer);
			return "redirect:/customers/" + updatedCustomer.getCustomerId();
		}
	}
	
	@PostMapping(value = "/{customerId}/assign-service")
	@PreAuthorize("hasAuthority('ASSIGN_SERVICE')")
	public String assignServiceRate(@PathVariable Integer customerId, @RequestParam Integer serviceId) throws TaxFMException {
		customerService.assignServiceRate(customerId, serviceId);
		return "redirect:/customers/" + customerId;
	}
	
	@PostMapping(value = "/{customerId}/services/{serviceId}/remove-service")
	@PreAuthorize("hasAuthority('REMOVE_ASSIGNED_SERVICE')")
	public String removeAssignedServiceRate(@PathVariable Integer customerId, @PathVariable Integer serviceId) throws TaxFMException {
		customerService.removeAssignServiceRate(customerId, serviceId);
		return "redirect:/customers/" + customerId;
	}
	
	@PostMapping("/{customerId}/toggle-lock")
	@PreAuthorize("hasAuthority('LOCK_UNLOCK_ACCOUNT')")
	public String toggleLock(@PathVariable Integer customerId) throws TaxFMException {
		customerService.toggleLock(customerId);
		return "redirect:/customers/" + customerId;
	}
	
	@PostMapping("/{customerId}/toggle-enable")
	@PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNT')")
	public String toggleEnable(@PathVariable Integer customerId) throws TaxFMException {
		customerService.toggleEnable(customerId);
		return "redirect:/customers/" + customerId;
	}
	
	@PostMapping("/{customerId}/reset-password")
	@PreAuthorize("hasAuthority('RESET_PASSWORD')")
	public String resetPassword(@PathVariable Integer customerId, @RequestParam String newPassword) throws TaxFMException {
		Integer loggedInEmployeeId = customerService.getLoggedInCustomer().getCustomerId();
		if(Objects.equals(loggedInEmployeeId, customerId)) {
			customerService.resetPassword(customerId, newPassword);
			return "redirect:/profile";
		}
		return "redirect:/access-denied";
	}
	
	@GetMapping("/{customerId}/delete")
	@PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
	public String deleteCustomer(@PathVariable Integer customerId) throws TaxFMException {
		customerService.deleteCustomerById(customerId);
		return "redirect:/customers";
	}
}