package com.beamsoftsolution.taxfm.utils;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TaxFMUtils {
	
	public void setCompanyAttributes(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.COMPANY_NAME);
	}
	
	public Set<String> getEmployeeAuthorities(Employee employee) {
		
		return Stream.concat(
				             // Stream roles
				             employee.getRoles().stream()
				                     .flatMap(role -> role.getAuthorities().stream())
				                     .map(Authority::getAuthority),
				             // Stream Authorities
				             employee.getAuthorities().stream()
				                     .map(Authority::getAuthority))
		             .collect(Collectors.toSet());
	}
}