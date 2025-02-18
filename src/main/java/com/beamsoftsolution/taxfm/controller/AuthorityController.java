package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.service.AuthorityService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.AUTHORITIES)
@Slf4j
public class AuthorityController {
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	public String getAllAuthorities(Model model, Authentication authentication) {
		if(authentication != null) {
			taxFMUtils.setCompanyAttributes(model);
			List<Authority> authorities = authorityService.getAllAuthorities();
			model.addAttribute("authorities", authorities);
			return "authorities/authorities";
		}
		return "redirect:/login";
	}
}