package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.model.Role;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.service.RoleService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.ROLES)
@Slf4j
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	@PreAuthorize("hasAuthority('VIEW_ALL_ROLES')")
	public String getAllRoles(Model model) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		
		// Fetch the logged-in employee
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		Integer loggedInEmployeeId = loggedInEmployee.getEmployeeId();
		model.addAttribute("loggedInEmployeeId", loggedInEmployeeId);
		
		Set<String> loggedInAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		
		// Fetch roles based on authorities
		List<Role> roles;
		boolean showNewRoleAddition;
		
		if(loggedInAuthorities.contains("VIEW_ALL_ROLES")) {
			roles = roleService.getAllRoles();
			long totalRolesCount = roleService.getTotalRolesCount();
			showNewRoleAddition = totalRolesCount < Constants.MAX_ROLES;
			
		}
		else {
			return "redirect:/access-denied";
		}
		model.addAttribute("roles", roles);
		model.addAttribute("showNewRoleAddition", showNewRoleAddition);
		return "roles/roles";
	}
}