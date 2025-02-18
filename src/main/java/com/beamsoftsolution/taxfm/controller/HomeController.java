package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@Slf4j
public class HomeController {
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	public String index(Model model, Authentication authentication) {
		if(authentication != null) {
			return "redirect:/home";
		}
		taxFMUtils.setCompanyAttributes(model);
		model.addAttribute("activeTab", "employee-login");
		return "landing/index";
	}
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(value = "activeTab", defaultValue = "employee-login") String activeTab) {
		taxFMUtils.setCompanyAttributes(model);
		model.addAttribute("activeTab", activeTab);
		return "landing/index";
	}
	
	@GetMapping(EndPoint.HOME)
	public String home(Model model) {
		taxFMUtils.setCompanyAttributes(model);
		return "redirect:/profile";
	}
	
	@GetMapping("/login-error")
	public String loginError(HttpServletRequest httpServletRequest, Model model) {
		Object loginErrorMessage = httpServletRequest.getSession().getAttribute("loginErrorMessage");
		if(loginErrorMessage != null) {
			model.addAttribute("loginErrorMessage", loginErrorMessage.toString());
		}
		taxFMUtils.setCompanyAttributes(model);
		model.addAttribute("loginError", true);
		String loginType = (String) httpServletRequest.getSession().getAttribute("loginType");
		model.addAttribute("activeTab", "employee".equals(loginType) ? "employee-login" : "customer-login");
		return "landing/index";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied(Model model) {
		taxFMUtils.setCompanyAttributes(model);
		return "landing/access-denied";
	}
}