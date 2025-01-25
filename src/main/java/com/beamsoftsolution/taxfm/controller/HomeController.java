package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
@Slf4j
public class HomeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public String index(Model model, Authentication authentication) {
		if(authentication != null) {
			return "redirect:/home";
		}
		model.addAttribute("title", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		return "landing/index";
	}
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(value = "tab", defaultValue = "employee-login") String tab) {
		model.addAttribute("title", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("activeTab", tab);
		return "landing/index";
	}
	
	@PostMapping("/login")
	public String loginSuccessEmployee(Authentication authentication, RedirectAttributes redirectAttributes) {
		if(authentication != null) {
			return "redirect:/home";
		}
		redirectAttributes.addAttribute("tab", "employee-login");
		return "redirect:/login-error";
	}
	
	@GetMapping("/login-error")
	public String loginError(HttpServletRequest httpServletRequest, Model model) {
		Object loginErrorMessage = httpServletRequest.getSession().getAttribute("loginErrorMessage");
		if(loginErrorMessage != null) {
			model.addAttribute("loginErrorMessage", loginErrorMessage.toString());
		}
		model.addAttribute("title", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("loginError", true);
		return "landing/index";
	}
	
	@GetMapping(EndPoint.HOME)
	public String home(Model model, Authentication authentication) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		if(authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Employee employee = employeeService.getLoggedInEmployee();
			if(employee != null) {
				model.addAttribute("employee", employee);
				return "home/employee-dashboard";
			}
			
			Customer customer = customerService.getLoggedInCustomer();
			if(customer != null) {
				model.addAttribute("customer", customer);
				return "home/customer-dashboard";
			}
		}
		return "redirect:/login";
	}
	
	@GetMapping("/career")
	public String careers(Model model) {
		model.addAttribute("title", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		return "landing/career";
	}
	
	@PostMapping("/submit-application")
	public String submitApplication(@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("position") String position,
			@RequestParam("resume") MultipartFile resume) {
		log.info("Received application from: " + name);
		return "redirect:/career?success";  // Redirect with a success query parameter
	}
}