package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.AuthorityService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(EndPoint.EMPLOYEES)
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AuthorityService authorityService;
	
	@GetMapping
	public String getAllEmployees(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		List<Employee> employees = employeeService.getAllEmployees();
		model.addAttribute("employees", employees);
		Integer loggedInEmployeeId = employeeService.getLoggedInEmployee().getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		long totalUsersCount = employeeService.getTotalEmployeesCount();
		if (totalUsersCount<Constants.MAX_EMPLOYEES) {
			model.addAttribute("showNewEmployeeAddition", true);
		} else {
			model.addAttribute("showNewEmployeeAddition", false);
		}
		return "employees/employees";
	}
	
//	@GetMapping
//	public String getAllEmployees(Model model, @PageableDefault(size = 10) Pageable pageable) {
//		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
//		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
//		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
//		Page<Employee> employees = employeeService.getPaginatedEmployees(pageable);
//		model.addAttribute("employees", employees);
//		return "employees/employees";
//	}
	
	@GetMapping("/add")
	public String showAddEmployeePage(Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("employee", new Employee());
		List<Authority> availableAuthorities = authorityService.getAllAuthorities();
		model.addAttribute("availableAuthorities", availableAuthorities);
		return "employees/addEmployee";
	}
	
	@PostMapping("/add")
	public String addEmployee(@ModelAttribute("employee") Employee employee) throws TaxFMException {
		log.info(employee.toString());
		employeeService.addEmployee(employee);
		return "redirect:/employees";
	}
	
	@GetMapping("/{employeeId}")
	public String viewEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Employee employee = employeeService.getEmployeeById(employeeId);
		model.addAttribute("employee", employee);
		return "employees/employee";
	}
	
	@GetMapping("/{employeeId}/edit")
	public String editEmployee(@PathVariable Integer employeeId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Employee employee = employeeService.getEmployeeById(employeeId);
		model.addAttribute("employee", employee);
		List<Authority> availableAuthorities = authorityService.getAllAuthorities();
		model.addAttribute("availableAuthorities", availableAuthorities);
		return "employees/editEmployee";
	}
	
	@PostMapping("/{employeeId}/edit")
	public String updateEmployee(@PathVariable Integer employeeId, @ModelAttribute("employee") Employee employee) throws TaxFMException {
		employeeService.updateEmployee(employee);
		return "redirect:/employees";
	}
	
	@GetMapping("/{employeeId}/delete")
	public String deleteEmployee(@PathVariable Integer employeeId) throws TaxFMException {
		employeeService.deleteEmployeeById(employeeId);
		return "redirect:/employees";
	}
}