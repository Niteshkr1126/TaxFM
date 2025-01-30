package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(EndPoint.CUSTOMERS)
public class CustomerController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SENIOR')")
	public String getAllCustomers(Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		// Determine role-based visibility
		List<Customer> customers;
		if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			// If the user is an ADMIN, fetch all customers
			customers = customerService.getAllCustomers();
			// Add logic for displaying the "New Customer Addition" button
			long totalUsersCount = customerService.getTotalCustomersCount();
			model.addAttribute("showNewCustomerAddition", totalUsersCount < Constants.MAX_CUSTOMERS);
		}
		else if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("SENIOR")) || loggedInEmployee.getAuthorities().stream().anyMatch(
				auth -> auth.getAuthority().equals("JUNIOR"))) {
			// If the user is either a SENIOR or JUNIOR, fetch only their assigned customers
			customers = loggedInEmployee.getAssignedCustomers().stream().toList();
			model.addAttribute("showNewCustomerAddition", false);
		}
		else {
			return "redirect:/access-denied";
		}
		model.addAttribute("customers", customers);
		return "customers/customers";
	}
	
	@GetMapping("/{customerId}")
	public String viewCustomer(@PathVariable Integer customerId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		// Check if the logged-in employee is assigned to the requested customer
		boolean hasAccess = loggedInEmployee.getAssignedCustomers().stream().anyMatch(customer -> customer.getCustomerId().equals(customerId));
		
		if(!hasAccess) {
			return "redirect:/access-denied";
		}
		
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
		return "customers/customer";
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showCustomerForm(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("customer", new Customer());
		return "customers/addCustomer";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String addCustomer(Customer customer, Errors errors) throws TaxFMException {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/addCustomer";
		}
		else {
			log.info(customer.toString());
			Customer addedCustomer = customerService.addCustomer(customer);
			return "redirect:/customers/" + addedCustomer.getCustomerId();
		}
	}
	
	@GetMapping("/{customerId}/edit")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String editCustomerForm(@PathVariable Integer customerId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
		return "customers/editCustomer";
	}
	
	@PostMapping("/{customerId}/edit")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String editCustomer(Customer customer, Errors errors) throws TaxFMException {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/editCustomer";
		}
		else {
			Customer updatedCustomer = customerService.updateCustomer(customer);
			return "redirect:/customers/" + updatedCustomer.getCustomerId();
		}
	}
	
	@PostMapping(value = "/{customerId}/edit", params = { "addServiceDetail" })
	@PreAuthorize("hasAuthority('ADMIN')")
	public String addServiceDetailInEdit(Customer customer, Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		if(customer != null) {
		//			if(customer.getServiceDetails() == null) {
		//				List<ServiceDetail> serviceDetails = new ArrayList<>();
		//				serviceDetails.add(new ServiceDetail());
		//				customer.setServiceDetails(serviceDetails);
		//			}
		//			else {
		//				customer.getServiceDetails().add(new ServiceDetail());
		//			}
		//		}
		return "customers/editCustomer";
	}
	
	@PostMapping(value = "/{customerId}/edit", params = { "removeServiceDetail" })
	@PreAuthorize("hasAuthority('ADMIN')")
	public String removeServiceDetailInEdit(Customer customer, Model model, HttpServletRequest request) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		ServiceDetail removeServiceDetail = customer.getServiceDetails().remove(Integer.parseInt(request.getParameter("removeServiceDetail")));
		//		if(removeServiceDetail.getServiceDetailId() != null) {
		//			serviceDetailService.deleteServiceDetailById(removeServiceDetail.getServiceDetailId());
		//		}
		return "customers/editCustomer";
	}
	
	@PostMapping(value = "/add", params = { "addServiceDetail" })
	@PreAuthorize("hasAuthority('ADMIN')")
	public String addServiceDetail(Customer customer, Model model, BindingResult bindingResult) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		if(customer != null) {
		//			if(customer.getServiceDetails() == null) {
		//				List<ServiceDetail> serviceDetails = new ArrayList<>();
		//				serviceDetails.add(new ServiceDetail());
		//				customer.setServiceDetails(serviceDetails);
		//			}
		//			else {
		//				customer.getServiceDetails().add(new ServiceDetail());
		//			}
		//		}
		return "customers/addCustomer";
	}
	
	@PostMapping(value = "/add", params = { "removeServiceDetail" })
	@PreAuthorize("hasAuthority('ADMIN')")
	public String removeServiceDetail(Customer customer, Model model, BindingResult bindingResult, HttpServletRequest request) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		customer.getServiceDetails().remove(Integer.parseInt(request.getParameter("removeServiceDetail")));
		return "customers/addCustomer";
	}
	
	@GetMapping("/{customerId}/delete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteCustomer(@PathVariable Integer customerId) throws TaxFMException {
		customerService.deleteCustomerById(customerId);
		return "redirect:/customers";
	}
}