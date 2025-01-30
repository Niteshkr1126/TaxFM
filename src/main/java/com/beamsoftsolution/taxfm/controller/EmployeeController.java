package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.AuthorityService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping(EndPoint.EMPLOYEES)
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	AuthorityService authorityService;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SENIOR')")
	public String getAllEmployees(Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		// Determine role-based visibility
		List<Employee> employees;
		if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			// If the user is an ADMIN, fetch all employees
			employees = employeeService.getAllEmployees();
			// Add logic for displaying the "New Employee Addition" button
			long totalUsersCount = employeeService.getTotalEmployeesCount();
			model.addAttribute("showNewEmployeeAddition", totalUsersCount < Constants.MAX_EMPLOYEES);
		}
		else if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("SENIOR"))) {
			// If the user is a SENIOR, fetch only employees with JUNIOR role // Update to display only subordinates
			employees = loggedInEmployee.getSubordinates();
			model.addAttribute("showNewEmployeeAddition", false);
		}
		else {
			return "redirect:/access-denied";
		}
		model.addAttribute("employees", employees);
		return "employees/employees";
	}
	
	@GetMapping("/{employeeId}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SENIOR')")
	public String viewEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		Employee employee;
		if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			employee = employeeService.getEmployeeById(employeeId);
			model.addAttribute("employee", employee);
		}
		else if(loggedInEmployee.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("SENIOR"))) {
			Employee targetEmployee = employeeService.getEmployeeById(employeeId);
			// Check if the targetEmployee exists and is a subordinate of the logged-in SENIOR
			if(targetEmployee != null && loggedInEmployee.getSubordinates().contains(targetEmployee)) {
				// If the employeeId is a subordinate, add the employee to the model
				model.addAttribute("employee", targetEmployee);
			}
			else {
				return "redirect:/access-denied";
			}
		}
		else {
			return "redirect:/access-denied";
		}
		// Get the list of subordinates
		List<Employee> availableEmployees = employeeService.getAvailableEmployeesForSubordination(employeeId);
		model.addAttribute("availableEmployees", availableEmployees);
		// Get the list of customers for the dropdown
		List<Customer> customers = employeeService.getAllCustomersForEmployee(loggedInEmployeeId);
		model.addAttribute("customers", customers);
		return "employees/employee";
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showAddEmployee(Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("employee", new Employee());
		List<Authority> availableAuthorities = authorityService.getAllAuthorities();
		
		// Remove the "CUSTOMER" authority from the list
		availableAuthorities = availableAuthorities.stream()
		                                           .filter(authority -> !authority.getAuthority().equals("CUSTOMER"))
		                                           .collect(Collectors.toList());
		
		model.addAttribute("availableAuthorities", availableAuthorities);
		return "employees/addEmployee";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String addEmployee(@ModelAttribute("employee") Employee employee) throws TaxFMException {
		log.info(employee.toString());
		employeeService.addEmployee(employee);
		return "redirect:/employees";
	}
	
	@GetMapping("/{employeeId}/edit")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String editEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Employee employee = employeeService.getEmployeeById(employeeId);
		model.addAttribute("employee", employee);
		List<Authority> availableAuthorities = authorityService.getAllAuthorities();
		
		// Remove the "CUSTOMER" authority from the list
		availableAuthorities = availableAuthorities.stream()
		                                           .filter(authority -> !authority.getAuthority().equals("CUSTOMER"))
		                                           .collect(Collectors.toList());
		
		model.addAttribute("availableAuthorities", availableAuthorities);
		return "employees/editEmployee";
	}
	
	@PostMapping("/{employeeId}/edit")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String updateEmployee(@PathVariable Integer employeeId, @ModelAttribute("employee") Employee employee) throws TaxFMException {
		employeeService.updateEmployee(employee);
		return "redirect:/employees";
	}
	
	@PostMapping("/assign-customer")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SENIOR')")
	public String assignCustomer(@RequestParam Integer employeeId, @RequestParam Integer customerId) throws TaxFMException {
		employeeService.assignCustomer(employeeId, customerId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/remove-customer")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SENIOR')")
	public String removeAssignedCustomer(@PathVariable Integer employeeId, @RequestParam Integer customerId) throws TaxFMException {
		employeeService.removeCustomerFromEmployee(employeeId, customerId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/subordinates")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String assignSubordinate(@PathVariable Integer employeeId, @RequestParam Integer subordinateId) throws TaxFMException {
		employeeService.addSubordinate(employeeId, subordinateId);
		return "redirect:/employees/" + employeeId;
	}
	
	// Remove Subordinate
	@PostMapping("/{employeeId}/subordinates/{subordinateId}/delete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String removeSubordinate(@PathVariable Integer employeeId, @PathVariable Integer subordinateId, RedirectAttributes redirectAttributes) throws TaxFMException {
		employeeService.removeSubordinate(employeeId, subordinateId);
		return "redirect:/employees/" + employeeId;
	}
	
	@GetMapping("/{employeeId}/delete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteEmployee(@PathVariable Integer employeeId) throws TaxFMException {
		employeeService.deleteEmployeeById(employeeId);
		return "redirect:/employees";
	}
}