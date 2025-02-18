package com.beamsoftsolution.taxfm.utils;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaxFMUtils {
	
	public void setCompanyAttributes(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.COMPANY_NAME);
	}
	
	public Set<String> getEmployeeAuthorities(Employee employee) {
		// Extract authorities from roles
		return employee.getRoles().stream()
		               .flatMap(role -> role.getAuthorities().stream()) // Get all authorities from roles
		               .map(Authority::getAuthority) // Extract authority names
		               .collect(Collectors.toSet());
	}
}