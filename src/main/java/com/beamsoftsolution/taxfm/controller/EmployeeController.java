package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.model.Role;
import com.beamsoftsolution.taxfm.service.AttendanceService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.service.RoleService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping(EndPoint.EMPLOYEES)
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AttendanceService attendanceService;
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('VIEW_ALL_EMPLOYEES', 'VIEW_ASSIGNED_SUBORDINATES')")
	public String getAllEmployees(Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		loggedInEmployee.setSubordinates(employeeService.getSubordinates(loggedInEmployeeId));
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		Set<String> loggedInAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		
		// Fetch employees based on authorities
		List<Employee> employees;
		boolean showNewEmployeeAddition = false;
		
		if(loggedInAuthorities.contains("VIEW_ALL_EMPLOYEES")) {
			employees = employeeService.getAllEmployees();
			long totalEmployeesCount = employeeService.getTotalEmployeesCount();
			showNewEmployeeAddition = totalEmployeesCount < Constants.MAX_EMPLOYEES;
		}
		else if(loggedInAuthorities.contains("VIEW_ASSIGNED_SUBORDINATES")) {
			employees = loggedInEmployee.getSubordinates();
		}
		else {
			return "redirect:/access-denied";
		}
		
		model.addAttribute("employees", employees);
		model.addAttribute("showNewEmployeeAddition", showNewEmployeeAddition);
		return "employees/employees";
	}
	
	@GetMapping("/{employeeId}")
	@PreAuthorize("hasAnyAuthority('VIEW_ALL_EMPLOYEES', 'VIEW_ASSIGNED_SUBORDINATES')")
	public String viewEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		loggedInEmployee.setSubordinates(employeeService.getSubordinates(loggedInEmployeeId));
		Set<String> loggedInAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		
		// Fetch the requested employee
		Employee employee = employeeService.getEmployeeById(employeeId);
		if(employee == null) {
			return "redirect:/access-denied"; // Prevents null reference issues
		}
		
		// Check if the employee is a subordinate by comparing IDs
		boolean isSubordinate = loggedInEmployee.getSubordinates().stream()
		                                        .anyMatch(sub -> sub.getEmployeeId().equals(employee.getEmployeeId()));
		
		// Check access permissions
		if(loggedInAuthorities.contains("VIEW_ALL_EMPLOYEES") ||
				(loggedInAuthorities.contains("VIEW_ASSIGNED_SUBORDINATES") && isSubordinate)) {
			employee.setSupervisor(employeeService.getSupervisor(employeeId));
			employee.setSubordinates(employeeService.getSubordinates(employeeId));
			employeeService.removeUnauthorisedCustomersFromEmployee(loggedInEmployee, employee);
			Set<String> employeeAuthorities = taxFMUtils.getEmployeeAuthorities(employee);
			model.addAttribute("employee", employee);
			model.addAttribute("employeeAuthorities", employeeAuthorities);
			model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
			// Get the list of subordinates
			List<Employee> availableEmployees = employeeService.getAvailableEmployeesForSubordination(loggedInEmployee, employee);
			model.addAttribute("availableEmployees", availableEmployees);
			// Get the list of customers for the dropdown
			List<Customer> customers = employeeService.getCustomersForEmployeeForAssignment(loggedInEmployee, employee);
			model.addAttribute("customers", customers);
			return "employees/employee";
		}
		return "redirect:/access-denied";
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('ADD_EMPLOYEE')")
	public String showAddEmployee(Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		model.addAttribute("employee", new Employee());
		List<Role> availableRoles = roleService.getAllRoles();
		
		// Remove the "CUSTOMER" role from the list
		availableRoles = availableRoles.stream().filter(role -> !role.getRole().equals("ROLE_CUSTOMER")).collect(Collectors.toList());
		
		model.addAttribute("availableRoles", availableRoles);
		return "employees/addEmployee";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADD_EMPLOYEE')")
	public String addEmployee(@ModelAttribute("employee") Employee employee) throws TaxFMException {
		Employee addedEmployee = employeeService.addEmployee(employee);
		return "redirect:/employees/" + addedEmployee.getEmployeeId();
	}
	
	@GetMapping("/{employeeId}/edit")
	@PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
	public String updateEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		Employee employee = employeeService.getEmployeeById(employeeId);
		model.addAttribute("employee", employee);
		List<Role> availableRoles = roleService.getAllRoles();
		
		// Remove the "CUSTOMER" role from the list
		availableRoles = availableRoles.stream().filter(role -> !role.getRole().equals("ROLE_CUSTOMER")).collect(Collectors.toList());
		
		model.addAttribute("availableRoles", availableRoles);
		return "employees/editEmployee";
	}
	
	@PostMapping("/{employeeId}/edit")
	@PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
	public String updateEmployee(@ModelAttribute("employee") Employee employee, Errors errors) throws TaxFMException {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "employees/editEmployee";
		}
		else {
			Employee updatedEmployee = employeeService.updateEmployee(employee);
			return "redirect:/employees/" + updatedEmployee.getEmployeeId();
		}
	}
	
	@PostMapping("/{employeeId}/subordinates")
	@PreAuthorize("hasAuthority('ASSIGN_SUBORDINATE')")
	public String assignSubordinate(@PathVariable Integer employeeId, @RequestParam Integer subordinateId) throws TaxFMException {
		employeeService.assignSubordinate(employeeId, subordinateId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/subordinates/{subordinateId}/delete")
	@PreAuthorize("hasAuthority('REMOVE_ASSIGNED_SUBORDINATE')")
	public String removeAssignedSubordinate(@PathVariable Integer employeeId, @PathVariable Integer subordinateId, RedirectAttributes redirectAttributes) throws TaxFMException {
		employeeService.removeAssignedSubordinate(employeeId, subordinateId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/assign-customer")
	@PreAuthorize("hasAuthority('ASSIGN_CUSTOMER')")
	public String assignCustomer(@RequestParam Integer employeeId, @RequestParam Integer customerId) throws TaxFMException {
		employeeService.assignCustomer(employeeId, customerId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/remove-customer")
	@PreAuthorize("hasAuthority('REMOVE_ASSIGNED_CUSTOMER')")
	public String removeAssignedCustomer(@PathVariable Integer employeeId, @RequestParam Integer customerId) throws TaxFMException {
		employeeService.removeAssignedCustomer(employeeId, customerId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/toggle-lock")
	@PreAuthorize("hasAuthority('LOCK_UNLOCK_ACCOUNT')")
	public String toggleLockUnlock(@PathVariable Integer employeeId) throws TaxFMException {
		employeeService.toggleLockUnlock(employeeId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/toggle-enable")
	@PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNT')")
	public String toggleEnableDisable(@PathVariable Integer employeeId) throws TaxFMException {
		employeeService.toggleEnableDisable(employeeId);
		return "redirect:/employees/" + employeeId;
	}
	
	@PostMapping("/{employeeId}/reset-password")
	@PreAuthorize("hasAuthority('RESET_PASSWORD')")
	public String resetPassword(@PathVariable Integer employeeId, @RequestParam String newPassword) throws TaxFMException {
		Integer loggedInEmployeeId = employeeService.getLoggedInEmployee().getEmployeeId();
		if(Objects.equals(loggedInEmployeeId, employeeId)) {
			employeeService.resetPassword(employeeId, newPassword);
			return "redirect:/profile";
		}
		return "redirect:/access-denied";
	}
	
	@GetMapping("/{employeeId}/delete")
	@PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
	public String deleteEmployee(@PathVariable Integer employeeId) throws TaxFMException {
		employeeService.deleteEmployeeById(employeeId);
		return "redirect:/employees";
	}
	
	@GetMapping(EndPoint.EMPLOYEE_ATTENDANCE)
	@PreAuthorize("hasAuthority('VIEW_EMPLOYEE_ATTENDANCE')")
	public String viewEmployeeAttendance(Model model, @PathVariable Integer employeeId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		Employee employee = employeeService.getEmployeeById(employeeId);
		LocalDate today = LocalDate.now();
		
		// If dates are not provided, default to the current date range
		if(startDate == null) {
			startDate = today;
		}
		if(endDate == null) {
			endDate = today;
		}
		
		// Validate date range
		if(startDate.isAfter(endDate)) {
			model.addAttribute("error", "End date cannot be before start date");
		}
		
		List<Attendance> attendances = attendanceService.getAttendanceByEmployeeAndDateRange(employee, startDate, endDate);
		
		model.addAttribute("employee", employee);
		model.addAttribute("attendances", attendances);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		return "employees/employeeAttendance";
	}
}